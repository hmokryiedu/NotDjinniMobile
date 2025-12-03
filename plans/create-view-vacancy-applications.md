# Plan: View Vacancy Applications Screen

**Created:** December 2024  
**Status:** Ready for implementation

---

## Overview

Implement `ViewVacancyApplicationsScreen` for employers to view all applications submitted for a specific vacancy. The screen will display a list of applications with job seeker information, application status, and navigation to view the full job seeker profile.

---

## Endpoint Information

**Endpoint:** `GET /application/vacancy/{vacancyId}?limit=20&offset=0`

**Response:**
```kotlin
@Serializable
data class ApplicationDetailsListResponse(
    @SerialName("applications")
    val applications: List<ApplicationDetailsResponse>,
)

@Serializable
data class ApplicationDetailsResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("vacancy")
    val vacancy: VacancyDetailsResponse,
    @SerialName("job_seeker")
    val jobSeeker: SeekerProfileResponse,
    @SerialName("status")
    val status: ApplicationStatusRequest,
    @SerialName("cover_letter")
    val coverLetter: String?,
    @SerialName("created_at")
    val createdAt: Instant,
    @SerialName("updated_at")
    val updatedAt: Instant
)
```

**Note:** `limit` and `offset` should be hardcoded to `20` and `0` for now.

---

## Phase 1: Network Layer

### 1.1 Create Application Details Response DTOs

**Location:** `app/src/main/kotlin/not/djinni/network/application/response/`

**ApplicationDetailsResponse.kt:**
```kotlin
@file:OptIn(ExperimentalTime::class)

package not.djinni.network.application.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import not.djinni.network.seeker.response.SeekerProfileResponse
import not.djinni.network.vacancy.response.VacancyDetailsResponse
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
data class ApplicationDetailsResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("vacancy")
    val vacancy: VacancyDetailsResponse,
    @SerialName("job_seeker")
    val jobSeeker: SeekerProfileResponse,
    @SerialName("status")
    val status: ApplicationStatusResponse,
    @SerialName("cover_letter")
    val coverLetter: String?,
    @SerialName("created_at")
    val createdAt: Instant,
    @SerialName("updated_at")
    val updatedAt: Instant
)
```

**ApplicationDetailsListResponse.kt:**
```kotlin
package not.djinni.network.application.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApplicationDetailsListResponse(
    @SerialName("applications")
    val applications: List<ApplicationDetailsResponse>
)
```

### 1.2 Update Application Resource

**Location:** `app/src/main/kotlin/not/djinni/network/application/resource/Application.kt`

Add new nested resource:
```kotlin
@Serializable
@Resource("vacancy/{vacancyId}")
data class ByVacancy(
    val parent: Application = Application(),
    val vacancyId: Long,
    val limit: Int? = null,
    val offset: Int? = null,
)
```

### 1.3 Update ApplicationDataSource Interface

**Location:** `app/src/main/kotlin/not/djinni/network/application/ApplicationDataSource.kt`

Add method:
```kotlin
suspend fun getApplicationsByVacancy(
    vacancyId: Long,
    limit: Int,
    offset: Int
): NetworkResponse<ApplicationDetailsListResponse>
```

### 1.4 Update DefaultApplicationDataSource

**Location:** `app/src/main/kotlin/not/djinni/network/application/DefaultApplicationDataSource.kt`

Add implementation:
```kotlin
override suspend fun getApplicationsByVacancy(
    vacancyId: Long,
    limit: Int,
    offset: Int
): NetworkResponse<ApplicationDetailsListResponse> {
    return httpClient
        .get(Application.ByVacancy(vacancyId = vacancyId, limit = limit, offset = offset))
        .networkResponse<ApplicationDetailsListResponse>()
}
```

---

## Phase 2: Domain Layer

### 2.1 Create Application Domain Model

**Location:** `app/src/main/kotlin/not/djinni/model/application/`

**ApplicationDetails.kt:**
```kotlin
package not.djinni.model.application

import not.djinni.model.seeker.SeekerProfile
import not.djinni.model.seeker.vacancy.Vacancy
import kotlin.time.Instant

data class ApplicationDetails(
    val id: Long,
    val vacancy: Vacancy,
    val jobSeeker: SeekerProfile,
    val status: ApplicationStatus,
    val coverLetter: String?,
    val createdAt: Instant,
    val updatedAt: Instant
)
```

**ApplicationStatus.kt:**
```kotlin
package not.djinni.model.application

enum class ApplicationStatus {
    APPLIED,
    REVIEWING,
    INTERVIEW,
    TEST_TASK,
    OFFER,
    HIRED,
    REJECTED,
    WITHDRAWN
}
```

### 2.2 Create Application Mapper

**Location:** `app/src/main/kotlin/not/djinni/model/application/ApplicationMapper.kt`

```kotlin
package not.djinni.model.application

import not.djinni.model.seeker.vacancy.toDomain
import not.djinni.model.seeker.toDomain
import not.djinni.network.application.response.ApplicationDetailsResponse
import not.djinni.network.application.response.ApplicationStatusResponse

fun ApplicationDetailsResponse.toDomain(): ApplicationDetails = ApplicationDetails(
    id = id,
    vacancy = vacancy.toDomain(),
    jobSeeker = jobSeeker.toDomain(),
    status = status.toDomain(),
    coverLetter = coverLetter,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun ApplicationStatusResponse.toDomain(): ApplicationStatus = when (this) {
    ApplicationStatusResponse.APPLIED -> ApplicationStatus.APPLIED
    ApplicationStatusResponse.REVIEWING -> ApplicationStatus.REVIEWING
    ApplicationStatusResponse.INTERVIEW -> ApplicationStatus.INTERVIEW
    ApplicationStatusResponse.TEST_TASK -> ApplicationStatus.TEST_TASK
    ApplicationStatusResponse.OFFER -> ApplicationStatus.OFFER
    ApplicationStatusResponse.HIRED -> ApplicationStatus.HIRED
    ApplicationStatusResponse.REJECTED -> ApplicationStatus.REJECTED
    ApplicationStatusResponse.WITHDRAWN -> ApplicationStatus.WITHDRAWN
}
```

### 2.3 Update ApplicationRepository Interface

**Location:** `app/src/main/kotlin/not/djinni/domain/repository/ApplicationRepository.kt`

Add method:
```kotlin
suspend fun getApplicationsByVacancy(vacancyId: Long): Result<List<ApplicationDetails>>
```

### 2.4 Update DefaultApplicationRepository

**Location:** `app/src/main/kotlin/not/djinni/data/repository/DefaultApplicationRepository.kt`

Add implementation:
```kotlin
override suspend fun getApplicationsByVacancy(
    vacancyId: Long
): Result<List<ApplicationDetails>> = runCatching {
    when (val response = applicationDataSource.getApplicationsByVacancy(
        vacancyId = vacancyId,
        limit = DEFAULT_LIMIT,
        offset = DEFAULT_OFFSET
    )) {
        is NetworkResponse.Success -> response.data.applications.map { it.toDomain() }
        is NetworkResponse.Error -> throw Exception(response.error)
    }
}

private companion object {
    const val DEFAULT_LIMIT = 20
    const val DEFAULT_OFFSET = 0
}
```

---

## Phase 3: Presentation Components

### 3.1 Create ApplicationCard Component

**Location:** `app/src/main/kotlin/not/djinni/presentation/core/components/base/ApplicationCard.kt`

Design pattern similar to `VacancyCard`:
- Job seeker name (body1Bold)
- Speciality (body2)
- Experience years (body3)
- Application status with colored indicator
- Cover letter preview (if exists, truncated)
- Applied date
- Uses `clickableNoRipple`

**ApplicationCardData Model:**

**Location:** `app/src/main/kotlin/not/djinni/presentation/core/components/base/model/ApplicationCardData.kt`

```kotlin
package not.djinni.presentation.core.components.base.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class ApplicationCardData(
    val id: Long,
    val seekerId: Long,
    val seekerName: TextData,
    val speciality: TextData,
    val experienceYears: TextData,
    val status: TextData,
    val statusColor: Color,
    val coverLetterPreview: TextData?,
    val appliedDate: TextData,
)
```

---

## Phase 4: Presentation Layer

### 4.1 Create ViewVacancyApplicationsState

**Location:** `app/src/main/kotlin/not/djinni/presentation/screens/employer/vacancy/applications/ViewVacancyApplicationsState.kt`

```kotlin
package not.djinni.presentation.screens.employer.vacancy.applications

import androidx.compose.runtime.Immutable
import not.djinni.presentation.core.components.base.model.ApplicationCardData
import not.djinni.presentation.core.components.base.model.TextData

@Immutable
internal data class ViewVacancyApplicationsState(
    val contentState: ApplicationsContentState = ApplicationsContentState.Loading,
)

internal sealed interface ApplicationsContentState {
    data object Loading : ApplicationsContentState
    data class Error(val message: TextData) : ApplicationsContentState
    data object Empty : ApplicationsContentState
    data class Data(val applications: List<ApplicationCardData>) : ApplicationsContentState
    
    val items: List<ApplicationCardData>
        get() = when (this) {
            is Data -> applications
            else -> emptyList()
        }
}
```

### 4.2 Create ViewVacancyApplicationsAction

**Location:** `app/src/main/kotlin/not/djinni/presentation/screens/employer/vacancy/applications/ViewVacancyApplicationsAction.kt`

```kotlin
package not.djinni.presentation.screens.employer.vacancy.applications

internal sealed interface ViewVacancyApplicationsAction {
    data object NavigateBack : ViewVacancyApplicationsAction
    data object Retry : ViewVacancyApplicationsAction
    data class OpenSeekerProfile(val seekerId: Long) : ViewVacancyApplicationsAction
}
```

### 4.3 Create ViewVacancyApplicationsSideEffect

**Location:** `app/src/main/kotlin/not/djinni/presentation/screens/employer/vacancy/applications/ViewVacancyApplicationsSideEffect.kt`

```kotlin
package not.djinni.presentation.screens.employer.vacancy.applications

internal sealed interface ViewVacancyApplicationsSideEffect {
    data object NavigateBack : ViewVacancyApplicationsSideEffect
    data class NavigateToSeekerProfile(val seekerId: Long) : ViewVacancyApplicationsSideEffect
}
```

### 4.4 Create ViewVacancyApplicationsViewModel

**Location:** `app/src/main/kotlin/not/djinni/presentation/screens/employer/vacancy/applications/ViewVacancyApplicationsViewModel.kt`

```kotlin
@file:OptIn(ExperimentalTime::class)

package not.djinni.presentation.screens.employer.vacancy.applications

import kotlinx.coroutines.flow.asSharedFlow
import not.djinni.R
import not.djinni.core.extension.mutableSideEffect
import not.djinni.core.extension.toFormattedFullDate
import not.djinni.domain.repository.ApplicationRepository
import not.djinni.model.application.ApplicationDetails
import not.djinni.model.application.ApplicationStatus
import not.djinni.presentation.core.StateViewModel
import not.djinni.presentation.core.components.base.model.ApplicationCardData
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.theme.NotDjinniColor
import not.djinni.utils.string.StringProvider
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam
import kotlin.time.ExperimentalTime

@KoinViewModel
internal class ViewVacancyApplicationsViewModel(
    @InjectedParam private val vacancyId: Long,
    private val applicationRepository: ApplicationRepository,
    private val stringProvider: StringProvider,
) : StateViewModel<ViewVacancyApplicationsState>(ViewVacancyApplicationsState()) {

    private val _sideEffect = mutableSideEffect<ViewVacancyApplicationsSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        loadApplications()
    }

    fun sendAction(action: ViewVacancyApplicationsAction) {
        when (action) {
            ViewVacancyApplicationsAction.NavigateBack -> {
                _sideEffect.tryEmit(ViewVacancyApplicationsSideEffect.NavigateBack)
            }
            ViewVacancyApplicationsAction.Retry -> loadApplications()
            is ViewVacancyApplicationsAction.OpenSeekerProfile -> {
                _sideEffect.tryEmit(ViewVacancyApplicationsSideEffect.NavigateToSeekerProfile(action.seekerId))
            }
        }
    }

    private fun loadApplications() {
        launch {
            updateState { copy(contentState = ApplicationsContentState.Loading) }

            applicationRepository.getApplicationsByVacancy(vacancyId).fold(
                onSuccess = { applications ->
                    val contentState = if (applications.isEmpty()) {
                        ApplicationsContentState.Empty
                    } else {
                        ApplicationsContentState.Data(applications.map { it.toCardData() })
                    }
                    updateState { copy(contentState = contentState) }
                },
                onFailure = {
                    updateState {
                        copy(contentState = ApplicationsContentState.Error(
                            message = R.string.vacancy_applications_error.toTextData()
                        ))
                    }
                }
            )
        }
    }

    private fun ApplicationDetails.toCardData(): ApplicationCardData {
        val experienceText = stringProvider.getString(
            R.string.vacancy_years_experience,
            jobSeeker.experienceYears
        )
        return ApplicationCardData(
            id = id,
            seekerId = jobSeeker.id,
            seekerName = jobSeeker.speciality.toTextData(), // Using speciality as name placeholder
            speciality = jobSeeker.speciality.toTextData(),
            experienceYears = experienceText.toTextData(),
            status = status.toDisplayText(),
            statusColor = status.toColor(),
            coverLetterPreview = coverLetter?.take(COVER_LETTER_PREVIEW_LENGTH)?.toTextData(),
            appliedDate = createdAt.toFormattedFullDate().toTextData()
        )
    }

    private fun ApplicationStatus.toDisplayText() = when (this) {
        ApplicationStatus.APPLIED -> R.string.application_status_applied
        ApplicationStatus.REVIEWING -> R.string.application_status_reviewing
        ApplicationStatus.INTERVIEW -> R.string.application_status_interview
        ApplicationStatus.TEST_TASK -> R.string.application_status_test_task
        ApplicationStatus.OFFER -> R.string.application_status_offer
        ApplicationStatus.HIRED -> R.string.application_status_hired
        ApplicationStatus.REJECTED -> R.string.application_status_rejected
        ApplicationStatus.WITHDRAWN -> R.string.application_status_withdrawn
    }.toTextData()

    private fun ApplicationStatus.toColor() = when (this) {
        ApplicationStatus.APPLIED -> NotDjinniColor.primary
        ApplicationStatus.REVIEWING -> NotDjinniColor.primary
        ApplicationStatus.INTERVIEW -> NotDjinniColor.primary
        ApplicationStatus.TEST_TASK -> NotDjinniColor.primary
        ApplicationStatus.OFFER -> NotDjinniColor.success
        ApplicationStatus.HIRED -> NotDjinniColor.success
        ApplicationStatus.REJECTED -> NotDjinniColor.error
        ApplicationStatus.WITHDRAWN -> NotDjinniColor.onSurface
    }

    private companion object {
        const val COVER_LETTER_PREVIEW_LENGTH = 100
    }
}
```

### 4.5 Create ViewVacancyApplicationsScreen

**Location:** `app/src/main/kotlin/not/djinni/presentation/screens/employer/vacancy/applications/ViewVacancyApplicationsScreen.kt`

```kotlin
package not.djinni.presentation.screens.employer.vacancy.applications

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import not.djinni.R
import not.djinni.presentation.core.Screen
import not.djinni.presentation.core.components.base.ApplicationCard
import not.djinni.presentation.core.components.base.FullscreenColumn
import not.djinni.presentation.core.components.base.HorizontalSpacer
import not.djinni.presentation.core.components.base.NotDjinniButton
import not.djinni.presentation.core.components.base.NotDjinniLoader
import not.djinni.presentation.core.components.base.NotDjinniText
import not.djinni.presentation.core.components.base.VerticalSpacer
import not.djinni.presentation.core.components.base.model.ButtonData
import not.djinni.presentation.core.components.base.model.TextData
import not.djinni.presentation.core.extension.clickableNoRipple
import not.djinni.presentation.core.extension.collectAsEffect
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.theme.NotDjinniIcons
import not.djinni.presentation.theme.NotDjinniTheme
import org.koin.core.parameter.parametersOf

@Composable
internal fun ViewVacancyApplicationsScreen(
    vacancyId: Long,
    onNavigateBack: () -> Unit,
    onNavigateToSeekerProfile: (Long) -> Unit,
) {
    Screen<ViewVacancyApplicationsViewModel>(
        parameters = { parametersOf(vacancyId) }
    ) { viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()

        Content(
            state = state,
            onAction = viewModel::sendAction
        )

        viewModel.sideEffect.collectAsEffect { effect ->
            when (effect) {
                ViewVacancyApplicationsSideEffect.NavigateBack -> onNavigateBack()
                is ViewVacancyApplicationsSideEffect.NavigateToSeekerProfile -> {
                    onNavigateToSeekerProfile(effect.seekerId)
                }
            }
        }
    }
}

@Composable
private fun Content(
    state: ViewVacancyApplicationsState,
    onAction: (ViewVacancyApplicationsAction) -> Unit = {},
) {
    FullscreenColumn {
        TopBar(onBack = { onAction(ViewVacancyApplicationsAction.NavigateBack) })
        when (val contentState = state.contentState) {
            is ApplicationsContentState.Loading -> LoadingContent()
            is ApplicationsContentState.Error -> ErrorContent(
                message = contentState.message,
                onRetry = { onAction(ViewVacancyApplicationsAction.Retry) }
            )
            is ApplicationsContentState.Empty -> EmptyContent()
            is ApplicationsContentState.Data -> DataContent(
                applications = contentState.applications,
                onApplicationClick = { seekerId ->
                    onAction(ViewVacancyApplicationsAction.OpenSeekerProfile(seekerId))
                }
            )
        }
    }
}

@Composable
private fun TopBar(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = NotDjinniTheme.offsets.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .size(ICON_SIZE)
                .clickableNoRipple(onClick = onBack),
            imageVector = NotDjinniIcons.back,
            contentDescription = null,
            tint = NotDjinniTheme.colors.onBackground
        )
        HorizontalSpacer(NotDjinniTheme.offsets.small)
        NotDjinniText(
            data = R.string.vacancy_applications_title.toTextData(),
            style = NotDjinniTheme.typography.title2,
            color = NotDjinniTheme.colors.onBackground
        )
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        NotDjinniLoader()
    }
}

@Composable
private fun ErrorContent(
    message: TextData,
    onRetry: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        NotDjinniText(
            data = message,
            style = NotDjinniTheme.typography.body1,
            color = NotDjinniTheme.colors.error
        )
        VerticalSpacer(NotDjinniTheme.offsets.medium)
        NotDjinniButton(
            data = ButtonData(text = R.string.retry.toTextData()),
            onClick = onRetry
        )
    }
}

@Composable
private fun EmptyContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        NotDjinniText(
            data = R.string.vacancy_applications_empty.toTextData(),
            style = NotDjinniTheme.typography.body2,
            color = NotDjinniTheme.colors.onBackground.copy(alpha = 0.6f)
        )
    }
}

@Composable
private fun DataContent(
    applications: List<ApplicationCardData>,
    onApplicationClick: (Long) -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(NotDjinniTheme.offsets.small)
    ) {
        items(
            items = applications,
            key = { it.id }
        ) { application ->
            ApplicationCard(
                modifier = Modifier.animateItem(),
                data = application,
                onClick = { onApplicationClick(application.seekerId) }
            )
        }
    }
}

@Composable
@Preview
private fun Preview() {
    NotDjinniTheme {
        val state = ViewVacancyApplicationsState(
            contentState = ApplicationsContentState.Empty
        )
        Content(state = state)
    }
}

private val ICON_SIZE = 24.dp
```

---

## Phase 5: Navigation

### 5.1 Update Screens.kt

**Location:** `app/src/main/kotlin/not/djinni/presentation/navigation/controller/Screens.kt`

Add new screen to Employer sealed interface:
```kotlin
@Serializable
data class VacancyApplications(val vacancyId: Long) : Employer
```

### 5.2 Update EmployerEntry.kt

**Location:** `app/src/main/kotlin/not/djinni/presentation/navigation/controller/EmployerEntry.kt`

Add entry:
```kotlin
entry<Screens.Employer.VacancyApplications> { entry ->
    ViewVacancyApplicationsScreen(
        vacancyId = entry.vacancyId,
        onNavigateBack = { controller.popBackStack() },
        onNavigateToSeekerProfile = { seekerId ->
            controller.navigate(Screens.Employer.ViewSeekerProfile(seekerId = seekerId))
        }
    )
}
```

### 5.3 Update VacancyDetailsScreen Entry (Optional)

Add navigation from VacancyDetails to VacancyApplications:
- Add "View Applications" button in employer VacancyDetailsScreen
- Update VacancyDetailsAction with `ViewApplications` action
- Update VacancyDetailsSideEffect with `NavigateToApplications(vacancyId)`

---

## Phase 6: String Resources

**Location:** `app/src/main/res/values/strings.xml`

Add:
```xml
<!-- Vacancy Applications -->
<string name="vacancy_applications_title">Applications</string>
<string name="vacancy_applications_error">Failed to load applications</string>
<string name="vacancy_applications_empty">No applications yet</string>
<string name="vacancy_years_experience">%d years experience</string>

<!-- Application Status -->
<string name="application_status_applied">Applied</string>
<string name="application_status_reviewing">Reviewing</string>
<string name="application_status_interview">Interview</string>
<string name="application_status_test_task">Test Task</string>
<string name="application_status_offer">Offer</string>
<string name="application_status_hired">Hired</string>
<string name="application_status_rejected">Rejected</string>
<string name="application_status_withdrawn">Withdrawn</string>

<!-- Application Card -->
<string name="application_cover_letter">Cover Letter</string>
<string name="application_applied_on">Applied on %s</string>
```

---

## File Checklist

### Network Layer
- [ ] `network/application/response/ApplicationDetailsResponse.kt`
- [ ] `network/application/response/ApplicationDetailsListResponse.kt`
- [ ] Update `network/application/resource/Application.kt` (add ByVacancy resource)
- [ ] Update `network/application/ApplicationDataSource.kt`
- [ ] Update `network/application/DefaultApplicationDataSource.kt`

### Domain Layer
- [ ] `model/application/ApplicationDetails.kt`
- [ ] `model/application/ApplicationStatus.kt`
- [ ] `model/application/ApplicationMapper.kt`
- [ ] Update `domain/repository/ApplicationRepository.kt`
- [ ] Update `data/repository/DefaultApplicationRepository.kt`

### Presentation - Components
- [ ] `presentation/core/components/base/ApplicationCard.kt`
- [ ] `presentation/core/components/base/model/ApplicationCardData.kt`

### Presentation - Screen
- [ ] `presentation/screens/employer/vacancy/applications/ViewVacancyApplicationsState.kt`
- [ ] `presentation/screens/employer/vacancy/applications/ViewVacancyApplicationsAction.kt`
- [ ] `presentation/screens/employer/vacancy/applications/ViewVacancyApplicationsSideEffect.kt`
- [ ] `presentation/screens/employer/vacancy/applications/ViewVacancyApplicationsViewModel.kt`
- [ ] `presentation/screens/employer/vacancy/applications/ViewVacancyApplicationsScreen.kt`

### Navigation
- [ ] Update `presentation/navigation/controller/Screens.kt`
- [ ] Update `presentation/navigation/controller/EmployerEntry.kt`

### Resources
- [ ] Update `res/values/strings.xml`

---

## UI Layout

```
┌─────────────────────────────────┐
│ ← Applications                   │
├─────────────────────────────────┤
│ ┌─────────────────────────────┐ │
│ │ Android Developer            │ │
│ │ 5 years experience           │ │
│ │ ● Applied                    │ │
│ │ Applied on Dec 1, 2024       │ │
│ └─────────────────────────────┘ │
│ ┌─────────────────────────────┐ │
│ │ iOS Developer                │ │
│ │ 3 years experience           │ │
│ │ ● Reviewing                  │ │
│ │ Cover letter preview...      │ │
│ │ Applied on Dec 2, 2024       │ │
│ └─────────────────────────────┘ │
│                                 │
│       (more cards...)           │
│                                 │
└─────────────────────────────────┘
```

---

## Implementation Order

1. **Network Layer** - DTOs, Resource, DataSource updates
2. **Domain Layer** - Models, Mapper, Repository updates
3. **Presentation Components** - ApplicationCard and model
4. **Presentation Screen** - State, Action, SideEffect, ViewModel, Screen
5. **Navigation** - Screens.kt and EmployerEntry.kt updates
6. **String Resources** - Add all required strings
7. **Integration** - Connect from VacancyDetailsScreen (optional)

---

## Notes

- Pagination is hardcoded (limit=20, offset=0) for now - can be enhanced later
- ViewSeekerProfile screen may need to be implemented or updated to accept seekerId parameter
- Consider adding pull-to-refresh functionality in future iterations
- Status colors use theme colors - ensure NotDjinniColor has `success` defined
- SeekerProfileResponse already exists and can be reused for mapping

---

## Dependencies

This implementation depends on:
- Existing `SeekerProfileResponse` in network layer
- Existing `VacancyDetailsResponse` in network layer  
- Existing `ApplicationStatusResponse` enum (rename from `ApplicationStatusRequest` if needed)
- Existing `toFormattedFullDate()` extension for Instant
- Existing `StringProvider` utility class
