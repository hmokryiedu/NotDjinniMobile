# Plan: View Vacancy Applications Screen

**Created:** December 2024  
**Status:** ✅ Implemented

---

## Overview

Implement `ViewVacancyApplicationsScreen` for employers to view all applications submitted for a specific vacancy. The screen will display a list of applications with job seeker information, application status, and navigation to view application details.

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
    val status: ApplicationStatusResponse,
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
@file:OptIn(ExperimentalTime::class)

package not.djinni.model.application

import not.djinni.model.seeker.SeekerProfile
import not.djinni.model.seeker.vacancy.Vacancy
import kotlin.time.ExperimentalTime
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

**Location:** `app/src/main/kotlin/not/djinni/data/mapper/ApplicationMapper.kt`

```kotlin
@file:OptIn(ExperimentalTime::class)

package not.djinni.data.mapper

import not.djinni.model.application.ApplicationDetails
import not.djinni.model.application.ApplicationStatus
import not.djinni.model.seeker.vacancy.toDomain
import not.djinni.network.application.response.ApplicationDetailsResponse
import not.djinni.network.application.response.ApplicationStatusResponse
import kotlin.time.ExperimentalTime

internal fun ApplicationDetailsResponse.toDomain(): ApplicationDetails = ApplicationDetails(
    id = id,
    vacancy = vacancy.toDomain(),
    jobSeeker = jobSeeker.toDomain(),
    status = status.toDomain(),
    coverLetter = coverLetter,
    createdAt = createdAt,
    updatedAt = updatedAt
)

internal fun ApplicationStatusResponse.toDomain(): ApplicationStatus = when (this) {
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

## Phase 3: Theme Update

### 3.1 Add Success Color to NotDjinniColor

**Location:** `app/src/main/kotlin/not/djinni/presentation/theme/NotDjinniColor.kt`

Add new color:
```kotlin
data class NotDjinniColor(
    val primary: Color = Color.White,
    val onPrimary: Color = Color(0xFF1B1C1E),
    val background: Color = Color(0xFF1B1C1E),
    val onBackground: Color = Color.White,
    val surface: Color = Color(0xFF1B1C1E),
    val onSurface: Color = Color.White,
    val surfaceContainer: Color = Color(0xFF1B1C1E),
    val forcedBlack: Color = Color(0xFF1B1C1E),
    val error: Color = Color(0xFFF54927),
    val success: Color = Color(0xFF4CAF50),  // NEW - Green color for positive statuses
    val highlightedContainer: Color = Color(0xFF282828),
)
```

---

## Phase 4: Presentation Components

### 4.1 Create ApplicationCard Component

**Location:** `app/src/main/kotlin/not/djinni/presentation/core/components/base/ApplicationCard.kt`

Design pattern similar to `VacancyCard`:
- Job seeker speciality (body1Bold)
- Experience years (body2)
- Application status with colored indicator
- Cover letter preview (if exists, truncated)
- Applied date
- Uses `clickableNoRipple`

```kotlin
package not.djinni.presentation.core.components.base

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import not.djinni.presentation.core.components.base.model.ApplicationCardData
import not.djinni.presentation.core.extension.clickableNoRipple
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
fun ApplicationCard(
    modifier: Modifier = Modifier,
    data: ApplicationCardData,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = NotDjinniTheme.colors.onSurface,
                shape = NotDjinniTheme.shapes.small,
            )
            .background(
                color = NotDjinniTheme.colors.primary.copy(alpha = 0.3f),
                shape = NotDjinniTheme.shapes.small,
            )
            .clickableNoRipple(onClick = onClick)
            .padding(
                horizontal = NotDjinniTheme.offsets.medium,
                vertical = NotDjinniTheme.offsets.small,
            ),
    ) {
        NotDjinniText(
            data = data.speciality,
            style = NotDjinniTheme.typography.body1Bold,
            color = NotDjinniTheme.colors.onBackground,
        )
        VerticalSpacer(NotDjinniTheme.offsets.tiny)
        NotDjinniText(
            data = data.experienceYears,
            style = NotDjinniTheme.typography.body2,
            color = NotDjinniTheme.colors.onBackground.copy(alpha = 0.7f),
        )
        VerticalSpacer(NotDjinniTheme.offsets.tiny)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                modifier = Modifier
                    .size(STATUS_INDICATOR_SIZE)
                    .clip(CircleShape),
                color = data.statusColor
            ) {}
            HorizontalSpacer(NotDjinniTheme.offsets.tiny)
            NotDjinniText(
                data = data.status,
                style = NotDjinniTheme.typography.body3,
                color = data.statusColor,
            )
        }
        if (data.coverLetterPreview != null) {
            VerticalSpacer(NotDjinniTheme.offsets.tiny)
            NotDjinniText(
                data = data.coverLetterPreview,
                style = NotDjinniTheme.typography.body3,
                color = NotDjinniTheme.colors.onBackground.copy(alpha = 0.6f),
                maxLines = 2,
            )
        }
        VerticalSpacer(NotDjinniTheme.offsets.tiny)
        NotDjinniText(
            data = data.appliedDate,
            style = NotDjinniTheme.typography.body3,
            color = NotDjinniTheme.colors.onBackground.copy(alpha = 0.5f),
        )
    }
}

private val STATUS_INDICATOR_SIZE = 8.dp
```

### 4.2 Create ApplicationCardData Model

**Location:** `app/src/main/kotlin/not/djinni/presentation/core/components/base/model/ApplicationCardData.kt`

```kotlin
package not.djinni.presentation.core.components.base.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class ApplicationCardData(
    val id: Long,
    val speciality: TextData,
    val experienceYears: TextData,
    val status: TextData,
    val statusColor: Color,
    val coverLetterPreview: TextData?,
    val appliedDate: TextData,
)
```

---

## Phase 5: Presentation Layer

### 5.1 Create ViewVacancyApplicationsState

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

### 5.2 Create ViewVacancyApplicationsAction

**Location:** `app/src/main/kotlin/not/djinni/presentation/screens/employer/vacancy/applications/ViewVacancyApplicationsAction.kt`

```kotlin
package not.djinni.presentation.screens.employer.vacancy.applications

internal sealed interface ViewVacancyApplicationsAction {
    data object NavigateBack : ViewVacancyApplicationsAction
    data object Retry : ViewVacancyApplicationsAction
    data class OpenApplicationDetails(val applicationId: Long) : ViewVacancyApplicationsAction
}
```

### 5.3 Create ViewVacancyApplicationsSideEffect

**Location:** `app/src/main/kotlin/not/djinni/presentation/screens/employer/vacancy/applications/ViewVacancyApplicationsSideEffect.kt`

```kotlin
package not.djinni.presentation.screens.employer.vacancy.applications

internal sealed interface ViewVacancyApplicationsSideEffect {
    data object NavigateBack : ViewVacancyApplicationsSideEffect
    data class NavigateToApplicationDetails(val applicationId: Long) : ViewVacancyApplicationsSideEffect
}
```

### 5.4 Create ViewVacancyApplicationsViewModel

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
import not.djinni.presentation.theme.NotDjinniTheme
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
            is ViewVacancyApplicationsAction.OpenApplicationDetails -> {
                _sideEffect.tryEmit(
                    ViewVacancyApplicationsSideEffect.NavigateToApplicationDetails(action.applicationId)
                )
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
            speciality = jobSeeker.speciality.toTextData(),
            experienceYears = experienceText.toTextData(),
            status = status.toDisplayText(),
            statusColor = status.toColor(),
            coverLetterPreview = coverLetter?.take(COVER_LETTER_PREVIEW_LENGTH)?.plus("...")?.toTextData(),
            appliedDate = stringProvider.getString(
                R.string.application_applied_on,
                createdAt.toFormattedFullDate()
            ).toTextData()
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
        ApplicationStatus.APPLIED -> NotDjinniTheme.colors.primary
        ApplicationStatus.REVIEWING -> NotDjinniTheme.colors.primary
        ApplicationStatus.INTERVIEW -> NotDjinniTheme.colors.primary
        ApplicationStatus.TEST_TASK -> NotDjinniTheme.colors.primary
        ApplicationStatus.OFFER -> NotDjinniTheme.colors.success
        ApplicationStatus.HIRED -> NotDjinniTheme.colors.success
        ApplicationStatus.REJECTED -> NotDjinniTheme.colors.error
        ApplicationStatus.WITHDRAWN -> NotDjinniTheme.colors.onSurface.copy(alpha = 0.5f)
    }

    private companion object {
        const val COVER_LETTER_PREVIEW_LENGTH = 100
    }
}
```

**Note:** Colors are accessed via `NotDjinniTheme.colors` since they are instance properties, not static. The ViewModel will need to use default color values or pass them from the composable.

**Alternative approach for colors (recommended):**

Define color constants in the ViewModel:
```kotlin
private object StatusColors {
    val applied = Color.White
    val success = Color(0xFF4CAF50)
    val error = Color(0xFFF54927)
    val withdrawn = Color.White.copy(alpha = 0.5f)
}

private fun ApplicationStatus.toColor() = when (this) {
    ApplicationStatus.APPLIED, ApplicationStatus.REVIEWING,
    ApplicationStatus.INTERVIEW, ApplicationStatus.TEST_TASK -> StatusColors.applied
    ApplicationStatus.OFFER, ApplicationStatus.HIRED -> StatusColors.success
    ApplicationStatus.REJECTED -> StatusColors.error
    ApplicationStatus.WITHDRAWN -> StatusColors.withdrawn
}
```

### 5.5 Create ViewVacancyApplicationsScreen

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
import not.djinni.presentation.core.components.base.model.ApplicationCardData
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
    onNavigateToApplicationDetails: (Long) -> Unit,
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
                is ViewVacancyApplicationsSideEffect.NavigateToApplicationDetails -> {
                    onNavigateToApplicationDetails(effect.applicationId)
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
                onApplicationClick = { applicationId ->
                    onAction(ViewVacancyApplicationsAction.OpenApplicationDetails(applicationId))
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
                onClick = { onApplicationClick(application.id) }
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

## Phase 6: Navigation

### 6.1 Update Screens.kt

**Location:** `app/src/main/kotlin/not/djinni/presentation/navigation/controller/Screens.kt`

Add new screens to Employer sealed interface:
```kotlin
@Serializable
data class VacancyApplications(val vacancyId: Long) : Employer

@Serializable
data class ViewApplicationDetails(val applicationId: Long) : Employer
```

### 6.2 Update EmployerEntry.kt

**Location:** `app/src/main/kotlin/not/djinni/presentation/navigation/controller/EmployerEntry.kt`

Add entries:
```kotlin
entry<Screens.Employer.VacancyApplications> { entry ->
    ViewVacancyApplicationsScreen(
        vacancyId = entry.vacancyId,
        onNavigateBack = { controller.popBackStack() },
        onNavigateToApplicationDetails = { applicationId ->
            controller.navigate(Screens.Employer.ViewApplicationDetails(applicationId = applicationId))
        }
    )
}

entry<Screens.Employer.ViewApplicationDetails> { entry ->
    // TODO: Implement ViewApplicationDetailsScreen
    // ViewApplicationDetailsScreen(
    //     applicationId = entry.applicationId,
    //     onNavigateBack = { controller.popBackStack() }
    // )
}
```

### 6.3 Update Employer VacancyDetailsScreen

Add "View Applications" button and navigation.

**Update VacancyDetailsAction.kt:**
```kotlin
internal sealed interface VacancyDetailsAction {
    data object Retry : VacancyDetailsAction
    data object NavigateBack : VacancyDetailsAction
    data object ViewApplications : VacancyDetailsAction  // NEW
}
```

**Update VacancyDetailsSideEffect.kt:**
```kotlin
internal sealed interface VacancyDetailsSideEffect {
    data object NavigateBack : VacancyDetailsSideEffect
    data class NavigateToApplications(val vacancyId: Long) : VacancyDetailsSideEffect  // NEW
}
```

**Update VacancyDetailsViewModel.kt sendAction:**
```kotlin
is VacancyDetailsAction.ViewApplications -> {
    _sideEffect.tryEmit(VacancyDetailsSideEffect.NavigateToApplications(vacancyId))
}
```

**Update VacancyDetailsScreen.kt:**
Add button in DataContent and handle side effect.

**Update EmployerEntry.kt VacancyDetails entry:**
```kotlin
entry<Screens.Employer.VacancyDetails> { entry ->
    VacancyDetailsScreen(
        vacancyId = entry.vacancyId,
        onNavigateBack = { controller.popBackStack() },
        onNavigateToApplications = { vacancyId ->
            controller.navigate(Screens.Employer.VacancyApplications(vacancyId = vacancyId))
        }
    )
}
```

---

## Phase 7: String Resources

**Location:** `app/src/main/res/values/strings.xml`

Add:
```xml
<!-- Vacancy Applications -->
<string name="vacancy_applications_title">Applications</string>
<string name="vacancy_applications_error">Failed to load applications</string>
<string name="vacancy_applications_empty">No applications yet</string>
<string name="vacancy_view_applications">View Applications</string>

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
<string name="application_applied_on">Applied on %s</string>
```

---

## File Checklist

### Network Layer
- [ ] `network/application/response/ApplicationDetailsResponse.kt` (NEW)
- [ ] `network/application/response/ApplicationDetailsListResponse.kt` (NEW)
- [ ] Update `network/application/resource/Application.kt` (add ByVacancy resource)
- [ ] Update `network/application/ApplicationDataSource.kt`
- [ ] Update `network/application/DefaultApplicationDataSource.kt`

### Domain Layer
- [ ] `model/application/ApplicationDetails.kt` (NEW)
- [ ] `model/application/ApplicationStatus.kt` (NEW)
- [ ] `data/mapper/ApplicationMapper.kt` (NEW)
- [ ] Update `domain/repository/ApplicationRepository.kt`
- [ ] Update `data/repository/DefaultApplicationRepository.kt`

### Theme
- [ ] Update `presentation/theme/NotDjinniColor.kt` (add success color)

### Presentation - Components
- [ ] `presentation/core/components/base/ApplicationCard.kt` (NEW)
- [ ] `presentation/core/components/base/model/ApplicationCardData.kt` (NEW)

### Presentation - Screen
- [ ] `presentation/screens/employer/vacancy/applications/ViewVacancyApplicationsState.kt` (NEW)
- [ ] `presentation/screens/employer/vacancy/applications/ViewVacancyApplicationsAction.kt` (NEW)
- [ ] `presentation/screens/employer/vacancy/applications/ViewVacancyApplicationsSideEffect.kt` (NEW)
- [ ] `presentation/screens/employer/vacancy/applications/ViewVacancyApplicationsViewModel.kt` (NEW)
- [ ] `presentation/screens/employer/vacancy/applications/ViewVacancyApplicationsScreen.kt` (NEW)

### Presentation - VacancyDetails Updates
- [ ] Update `presentation/screens/employer/vacancy/details/VacancyDetailsAction.kt`
- [ ] Update `presentation/screens/employer/vacancy/details/VacancyDetailsSideEffect.kt`
- [ ] Update `presentation/screens/employer/vacancy/details/VacancyDetailsViewModel.kt`
- [ ] Update `presentation/screens/employer/vacancy/details/VacancyDetailsScreen.kt`

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
│ │ Looking forward to joining..│ │
│ │ Applied on Dec 2, 2024       │ │
│ └─────────────────────────────┘ │
│                                 │
│       (more cards...)           │
│                                 │
└─────────────────────────────────┘
```

---

## Implementation Order

1. **Theme** - Add success color to NotDjinniColor
2. **Network Layer** - DTOs, Resource, DataSource updates
3. **Domain Layer** - Models, Mapper, Repository updates
4. **Presentation Components** - ApplicationCard and model
5. **Presentation Screen** - State, Action, SideEffect, ViewModel, Screen
6. **Navigation** - Screens.kt and EmployerEntry.kt updates
7. **VacancyDetails Integration** - Add "View Applications" button and navigation
8. **String Resources** - Add all required strings

---

## Notes

- Pagination is hardcoded (limit=20, offset=0) for now - can be enhanced later
- Clicking on application card navigates to ViewApplicationDetails (to be implemented separately)
- Consider adding pull-to-refresh functionality in future iterations
- Success color (#4CAF50 - Material Green 500) added to theme for positive statuses
- Mapper placed in `data/mapper/` following existing pattern (SeekerProfileMapper)
- Use existing `ApplicationStatusResponse` enum from network layer

---

## Dependencies

This implementation depends on:
- Existing `SeekerProfileResponse` in network layer
- Existing `VacancyDetailsResponse` in network layer  
- Existing `ApplicationStatusResponse` enum
- Existing `toFormattedFullDate()` extension for Instant
- Existing `StringProvider` utility class
- Existing `SeekerProfileMapper` for `SeekerProfileResponse.toDomain()`
- Existing `VacancyMapper` for `VacancyDetailsResponse.toDomain()`

---

## Future Enhancements

- [ ] ViewApplicationDetailsScreen implementation
- [ ] Pagination support
- [ ] Pull-to-refresh
- [ ] Application status update functionality
- [ ] Filter/sort applications
