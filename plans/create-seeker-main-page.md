# Plan: Implement Seeker Main Screen with Vacancy Tabs

## Overview
Implement the main screen for seekers with two tabs ("All" and "Recommended" vacancies), search functionality, and vacancy list display.

---

## Phase 1: Network Layer

### 1.1 Create Vacancy Response Models
**Location:** `app/src/main/kotlin/not/djinni/network/vacancy/response/`

**VacancyDetailsResponse.kt:**
```kotlin
package not.djinni.network.vacancy.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import not.djinni.network.employer.response.CompanyResponse

@Serializable
data class VacancyDetailsResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("company")
    val company: CompanyResponse,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("salary_min")
    val salaryMin: Int,
    @SerialName("salary_max")
    val salaryMax: Int,
    @SerialName("min_experience_years")
    val minExperienceYears: Int?,
    @SerialName("employment_type")
    val employmentType: String?,
    @SerialName("category")
    val category: String?,
    @SerialName("status")
    val status: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String
)
```

**VacancyListResponse.kt:**
```kotlin
package not.djinni.network.vacancy.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VacancyListResponse(
    @SerialName("vacancies")
    val vacancies: List<VacancyDetailsResponse>
)
```

### 1.2 Create Vacancy Resource
**Location:** `app/src/main/kotlin/not/djinni/network/vacancy/resource/Vacancy.kt`

```kotlin
package not.djinni.network.vacancy.resource

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Serializable
@Resource("/vacancy")
class Vacancy(
    val limit: Int? = null,
    val offset: Int? = null,
    val search: String? = null
)
```

### 1.3 Create VacancyDataSource
**Location:** `app/src/main/kotlin/not/djinni/network/vacancy/`

**VacancyDataSource.kt:**
```kotlin
package not.djinni.network.vacancy

import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.vacancy.response.VacancyListResponse

interface VacancyDataSource {

    suspend fun getAllVacancies(
        limit: Int,
        offset: Int,
        search: String?
    ): NetworkResponse<VacancyListResponse>
}
```

**DefaultVacancyDataSource.kt:**
```kotlin
package not.djinni.network.vacancy

import io.ktor.client.HttpClient
import io.ktor.client.plugins.resources.get
import not.djinni.network.common.extension.networkResponse
import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.vacancy.resource.Vacancy
import not.djinni.network.vacancy.response.VacancyListResponse
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single(binds = [VacancyDataSource::class])
internal class DefaultVacancyDataSource(
    @Named("authenticated") private val httpClient: HttpClient
) : VacancyDataSource {

    override suspend fun getAllVacancies(
        limit: Int,
        offset: Int,
        search: String?
    ): NetworkResponse<VacancyListResponse> {
        return httpClient
            .get(Vacancy(limit = limit, offset = offset, search = search))
            .networkResponse<VacancyListResponse>()
    }
}
```

### 1.4 Update Seeker Resource
**Location:** `app/src/main/kotlin/not/djinni/network/seeker/resource/Seeker.kt`

Add nested Vacancy resource:
```kotlin
@Serializable
@Resource("vacancy")
class Vacancy(
    val parent: Seeker = Seeker(),
    val limit: Int? = null,
    val offset: Int? = null,
    val search: String? = null
)
```

### 1.5 Update SeekerDataSource
**Location:** `app/src/main/kotlin/not/djinni/network/seeker/SeekerDataSource.kt`

Add method:
```kotlin
suspend fun getRecommendedVacancies(
    limit: Int,
    offset: Int,
    search: String?
): NetworkResponse<VacancyListResponse>
```

### 1.6 Update DefaultSeekerDataSource
**Location:** `app/src/main/kotlin/not/djinni/network/seeker/DefaultSeekerDataSource.kt`

Add implementation:
```kotlin
override suspend fun getRecommendedVacancies(
    limit: Int,
    offset: Int,
    search: String?
): NetworkResponse<VacancyListResponse> {
    return httpClient
        .get(Seeker.Vacancy(limit = limit, offset = offset, search = search))
        .networkResponse<VacancyListResponse>()
}
```

---

## Phase 2: Domain Layer

### 2.1 Create Vacancy Domain Model
**Location:** `app/src/main/kotlin/not/djinni/model/vacancy/Vacancy.kt`

```kotlin
package not.djinni.model.seeker.vacancy

import not.djinni.model.company.Company

data class Vacancy(
    val id: Long,
    val company: Company,
    val title: String,
    val description: String,
    val salaryMin: Int,
    val salaryMax: Int,
    val minExperienceYears: Int?,
    val employmentType: String?,
    val category: String?,
    val status: String,
    val createdAt: String,
    val updatedAt: String
)
```

### 2.2 Create Vacancy Mapper
**Location:** `app/src/main/kotlin/not/djinni/model/vacancy/VacancyMapper.kt`

```kotlin
package not.djinni.model.seeker.vacancy

import not.djinni.model.company.Company
import not.djinni.network.vacancy.response.VacancyDetailsResponse

fun VacancyDetailsResponse.toDomain(): Vacancy = Vacancy(
    id = id,
    company = Company(
        id = company.id,
        name = company.name,
        website = company.website,
        description = company.description
    ),
    title = title,
    description = description,
    salaryMin = salaryMin,
    salaryMax = salaryMax,
    minExperienceYears = minExperienceYears,
    employmentType = employmentType,
    category = category,
    status = status,
    createdAt = createdAt,
    updatedAt = updatedAt
)
```

### 2.3 Create Use Cases
**Location:** `app/src/main/kotlin/not/djinni/domain/usecase/vacancy/`

**GetAllVacanciesUseCase.kt:**
```kotlin
package not.djinni.domain.usecase.vacancy

import not.djinni.domain.usecase.core.UseCaseWithParams
import not.djinni.model.seeker.vacancy.Vacancy
import not.djinni.model.seeker.vacancy.toDomain
import not.djinni.network.vacancy.VacancyDataSource
import org.koin.core.annotation.Factory

@Factory
class GetAllVacanciesUseCase(
    private val vacancyDataSource: VacancyDataSource,
) : UseCaseWithParams<Result<List<Vacancy>>, GetAllVacanciesUseCase.Params> {

    override suspend fun invoke(params: Params): Result<List<Vacancy>> = runCatching {
        val response = vacancyDataSource.getAllVacancies(
            limit = params.limit,
            offset = params.offset,
            search = params.search
        )
        response.data?.vacancies?.map { it.toDomain() } ?: emptyList()
    }

    data class Params(
        val limit: Int,
        val offset: Int,
        val search: String? = null
    )
}
```

**GetRecommendedVacanciesUseCase.kt:**
```kotlin
package not.djinni.domain.usecase.vacancy

import not.djinni.domain.usecase.core.UseCaseWithParams
import not.djinni.model.seeker.vacancy.Vacancy
import not.djinni.model.seeker.vacancy.toDomain
import not.djinni.network.seeker.SeekerDataSource
import org.koin.core.annotation.Factory

@Factory
class GetRecommendedVacanciesUseCase(
    private val seekerDataSource: SeekerDataSource,
) : UseCaseWithParams<Result<List<Vacancy>>, GetRecommendedVacanciesUseCase.Params> {

    override suspend fun invoke(params: Params): Result<List<Vacancy>> = runCatching {
        val response = seekerDataSource.getRecommendedVacancies(
            limit = params.limit,
            offset = params.offset,
            search = params.search
        )
        response.data?.vacancies?.map { it.toDomain() } ?: emptyList()
    }

    data class Params(
        val limit: Int,
        val offset: Int,
        val search: String? = null
    )
}
```

---

## Phase 3: Presentation Components

### 3.1 Create VacancyCard Component
**Location:** `app/src/main/kotlin/not/djinni/presentation/core/components/base/VacancyCard.kt`

Design based on `WorkExperienceItem` pattern:
- Company name (body2)
- Job title (body1Bold)
- Salary range display
- Employment type (if available)
- Experience requirement (if available)
- Uses `clickableNoRipple`
- Border and background matching app theme

### 3.2 Create NotDjinniTabBar Component
**Location:** `app/src/main/kotlin/not/djinni/presentation/core/components/base/NotDjinniTabBar.kt`

Custom tab bar with:
- Row layout with two tabs
- Selection indicator using background color
- Uses `clickableNoRipple` for selection
- Matches app theme colors

---

## Phase 4: Main Seeker Screen

### 4.1 Create VacancyTab Enum
**Location:** `app/src/main/kotlin/not/djinni/presentation/screens/seeker/main/model/VacancyTab.kt`

```kotlin
package not.djinni.presentation.screens.seeker.main.model

enum class VacancyTab {
    ALL,
    RECOMMENDED
}
```

### 4.2 Update MainSeekerState
**Location:** `app/src/main/kotlin/not/djinni/presentation/screens/seeker/main/MainSeekerState.kt`

```kotlin
package not.djinni.presentation.screens.seeker.main

import androidx.compose.runtime.Immutable
import not.djinni.model.seeker.vacancy.Vacancy
import not.djinni.presentation.screens.seeker.main.model.VacancyTab

@Immutable
internal data class MainSeekerState(
    val isLoading: Boolean = false,
    val selectedTab: VacancyTab = VacancyTab.ALL,
    val allVacancies: List<Vacancy> = emptyList(),
    val recommendedVacancies: List<Vacancy> = emptyList(),
    val isSearching: Boolean = false,
)
```

### 4.3 Update MainSeekerAction
**Location:** `app/src/main/kotlin/not/djinni/presentation/screens/seeker/main/MainSeekerAction.kt`

```kotlin
package not.djinni.presentation.screens.seeker.main

import not.djinni.presentation.screens.seeker.main.model.VacancyTab

internal sealed interface MainSeekerAction {
    data class SelectTab(val tab: VacancyTab) : MainSeekerAction
    data class Search(val query: String) : MainSeekerAction
    data class OpenVacancy(val vacancyId: Long) : MainSeekerAction
}
```

### 4.4 Update MainSeekerSideEffect
**Location:** `app/src/main/kotlin/not/djinni/presentation/screens/seeker/main/MainSeekerSideEffect.kt`

```kotlin
package not.djinni.presentation.screens.seeker.main

internal sealed interface MainSeekerSideEffect {
    data class NavigateToVacancyDetails(val vacancyId: Long) : MainSeekerSideEffect
}
```

### 4.5 Update MainSeekerViewModel
**Location:** `app/src/main/kotlin/not/djinni/presentation/screens/seeker/main/MainSeekerViewModel.kt`

- Inject `GetAllVacanciesUseCase` and `GetRecommendedVacanciesUseCase`
- On init: Load all vacancies
- On tab change: Clear search field, load appropriate list
- On search: Debounce query, call appropriate endpoint based on selected tab
- Emit side effect for navigation

### 4.6 Update MainSeekerScreen
**Location:** `app/src/main/kotlin/not/djinni/presentation/screens/seeker/main/MainSeekerScreen.kt`

UI Structure:
```
FullscreenColumn {
    NotDjinniText(title)
    VerticalSpacer
    NotDjinniTabBar(selectedTab, onTabSelect)
    VerticalSpacer
    NotDjinniTextField(searchState, placeholder)
    VerticalSpacer
    LazyColumn {
        items(vacancies, key = { it.id }) { vacancy ->
            VacancyCard(vacancy, onClick)
        }
    }
}
```

- Add `onVacancyClick: (Long) -> Unit` parameter
- Use `LaunchedEffect` with `snapshotFlow` for search debounce
- Clear search on tab change using `LaunchedEffect(state.selectedTab)`

---

## Phase 5: Navigation Updates

### 5.1 Update Screens.kt
**Location:** `app/src/main/kotlin/not/djinni/presentation/navigation/controller/Screens.kt`

Change `VacancyDetails` to accept parameter:
```kotlin
@Serializable
data class VacancyDetails(val vacancyId: Long) : Seeker
```

### 5.2 Update SeekerEntry.kt
**Location:** `app/src/main/kotlin/not/djinni/presentation/navigation/controller/SeekerEntry.kt`

- Add navigation callback to `MainSeekerScreen`:
```kotlin
entry<Screens.Seeker.Main> {
    MainSeekerScreen(
        onVacancyClick = { vacancyId ->
            controller.navigate(Screens.Seeker.VacancyDetails(vacancyId = vacancyId))
        }
    )
}
```

- Update `VacancyDetails` entry to receive parameter:
```kotlin
entry<Screens.Seeker.VacancyDetails> { screen ->
    VacancyDetailsScreen(vacancyId = screen.key.vacancyId)
}
```

---

## File Checklist

### Network Layer
- [ ] `network/vacancy/response/VacancyDetailsResponse.kt`
- [ ] `network/vacancy/response/VacancyListResponse.kt`
- [ ] `network/vacancy/resource/Vacancy.kt`
- [ ] `network/vacancy/VacancyDataSource.kt`
- [ ] `network/vacancy/DefaultVacancyDataSource.kt`
- [ ] Update `network/seeker/resource/Seeker.kt`
- [ ] Update `network/seeker/SeekerDataSource.kt`
- [ ] Update `network/seeker/DefaultSeekerDataSource.kt`

### Domain Layer
- [ ] `model/vacancy/Vacancy.kt`
- [ ] `model/vacancy/VacancyMapper.kt`
- [ ] `domain/usecase/vacancy/GetAllVacanciesUseCase.kt`
- [ ] `domain/usecase/vacancy/GetRecommendedVacanciesUseCase.kt`

### Presentation Layer
- [ ] `presentation/core/components/base/VacancyCard.kt`
- [ ] `presentation/core/components/base/NotDjinniTabBar.kt`
- [ ] `presentation/screens/seeker/main/model/VacancyTab.kt`
- [ ] Update `presentation/screens/seeker/main/MainSeekerState.kt`
- [ ] Update `presentation/screens/seeker/main/MainSeekerAction.kt`
- [ ] Update `presentation/screens/seeker/main/MainSeekerSideEffect.kt`
- [ ] Update `presentation/screens/seeker/main/MainSeekerViewModel.kt`
- [ ] Update `presentation/screens/seeker/main/MainSeekerScreen.kt`

### Navigation
- [ ] Update `presentation/navigation/controller/Screens.kt`
- [ ] Update `presentation/navigation/controller/SeekerEntry.kt`

---

## Constants
- `DEFAULT_LIMIT = 20`
- `DEFAULT_OFFSET = 0`
- Search debounce: 300ms

---

## String Resources
- `vacancy_tab_all` = "All"
- `vacancy_tab_recommended` = "Recommended"
- `vacancy_search_placeholder` = "Search vacancies..."
- `vacancies_title` = "Vacancies"
- `vacancy_no_results` = "No vacancies found"
