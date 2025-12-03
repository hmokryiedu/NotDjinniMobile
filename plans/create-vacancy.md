# Create Vacancy Screen Implementation Plan

**Created:** 2025-12-03
**Status:** Ready for implementation

---

## Overview

Implement a complete vacancy creation flow for employers with bottom sheet selections for Employment Type and Job Category, form validation, and navigation to the created vacancy details upon successful creation.

---

## Key Decisions

- **Shared Enums**: Use existing response enums (EmploymentTypeResponse, JobCategoryCodeResponse, VacancyStatusCodeResponse) for both requests and responses
- **Required Fields**: Employment Type, Experience Years, and Category are REQUIRED (not optional)
- **Status**: Always ACTIVE (hardcoded, not shown to user)
- **Alert Pattern**: Use AlertContainer with CreateVacancyAlert enum (same pattern as VacancyDetailsScreen)
- **Navigation**: After successful creation, navigate to VacancyDetails screen with the new vacancy ID

---

## Files to Modify/Create

### Layer 1: Network

| File | Action |
|------|--------|
| `network/vacancy/request/CreateVacancyRequest.kt` | Create new request DTO |
| `network/vacancy/VacancyDataSource.kt` | Add `createVacancy()` method |
| `network/vacancy/DefaultVacancyDataSource.kt` | Implement createVacancy |

### Layer 2: Domain

| File | Action |
|------|--------|
| `domain/repository/VacancyRepository.kt` | Add `createVacancy()` method |
| `domain/usecase/vacancy/CreateVacancyUseCase.kt` | Create new use case |

### Layer 3: Data

| File | Action |
|------|--------|
| `data/repository/DefaultVacancyRepository.kt` | Implement createVacancy |
| `data/mapper/VacancyMapper.kt` | Add enum mapping functions |

### Layer 4: Presentation

| File | Action |
|------|--------|
| `screens/employer/vacancy/create/CreateVacancyAlert.kt` | Create alert enum |
| `screens/employer/vacancy/create/CreateVacancyState.kt` | Update state |
| `screens/employer/vacancy/create/CreateVacancyAction.kt` | Update actions |
| `screens/employer/vacancy/create/CreateVacancySideEffect.kt` | Update side effects |
| `screens/employer/vacancy/create/CreateVacancyViewModel.kt` | Implement logic |
| `screens/employer/vacancy/create/CreateVacancyScreen.kt` | Implement UI |
| `screens/employer/vacancy/create/components/EmploymentTypeBottomSheet.kt` | Create bottom sheet |
| `screens/employer/vacancy/create/components/JobCategoryBottomSheet.kt` | Create bottom sheet |

### Layer 5: Navigation

| File | Action |
|------|--------|
| `navigation/controller/EmployerEntry.kt` | Update CreateVacancy entry |

---

## Implementation Details

### 1. Network Layer

#### CreateVacancyRequest.kt

```kotlin
package not.djinni.network.vacancy.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import not.djinni.network.vacancy.response.EmploymentTypeResponse
import not.djinni.network.vacancy.response.JobCategoryCodeResponse
import not.djinni.network.vacancy.response.VacancyStatusCodeResponse

@Serializable
data class CreateVacancyRequest(
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("salary_min")
    val salaryMin: Int,
    @SerialName("salary_max")
    val salaryMax: Int,
    @SerialName("min_experience_years")
    val minExperienceYears: Int,
    @SerialName("employment_type")
    val employmentType: EmploymentTypeResponse,
    @SerialName("category")
    val category: JobCategoryCodeResponse,
    @SerialName("status")
    val status: VacancyStatusCodeResponse
)
```

#### VacancyDataSource.kt

Add method:
```kotlin
suspend fun createVacancy(request: CreateVacancyRequest): NetworkResponse<VacancyDetailsResponse>
```

#### DefaultVacancyDataSource.kt

```kotlin
override suspend fun createVacancy(
    request: CreateVacancyRequest
): NetworkResponse<VacancyDetailsResponse> {
    return httpClient
        .post(Vacancy()) { setBody(request) }
        .networkResponse()
}
```

---

### 2. Domain Layer

#### VacancyRepository.kt

Add method:
```kotlin
suspend fun createVacancy(
    title: String,
    description: String,
    salaryMin: Int,
    salaryMax: Int,
    experienceYears: Int,
    employmentType: EmploymentType,
    category: JobCategoryCode
): Result<Vacancy>
```

#### CreateVacancyUseCase.kt

```kotlin
package not.djinni.domain.usecase.vacancy

import not.djinni.domain.repository.VacancyRepository
import not.djinni.domain.usecase.core.UseCaseWithParams
import not.djinni.model.seeker.vacancy.EmploymentType
import not.djinni.model.seeker.vacancy.JobCategoryCode
import not.djinni.model.seeker.vacancy.Vacancy
import org.koin.core.annotation.Factory

@Factory
class CreateVacancyUseCase(
    private val vacancyRepository: VacancyRepository,
) : UseCaseWithParams<Result<Vacancy>, CreateVacancyUseCase.Params> {

    override suspend fun invoke(params: Params): Result<Vacancy> = runCatching {
        require(params.title.isNotBlank()) { "Title cannot be empty" }
        require(params.description.isNotBlank()) { "Description cannot be empty" }
        require(params.salaryMin >= 0) { "Minimum salary must be non-negative" }
        require(params.salaryMax > 0) { "Maximum salary must be positive" }
        require(params.salaryMin < params.salaryMax) { "Minimum salary must be less than maximum salary" }
        require(params.experienceYears >= 0) { "Experience years must be non-negative" }

        vacancyRepository.createVacancy(
            title = params.title,
            description = params.description,
            salaryMin = params.salaryMin,
            salaryMax = params.salaryMax,
            experienceYears = params.experienceYears,
            employmentType = params.employmentType,
            category = params.category
        ).getOrThrow()
    }

    data class Params(
        val title: String,
        val description: String,
        val salaryMin: Int,
        val salaryMax: Int,
        val experienceYears: Int,
        val employmentType: EmploymentType,
        val category: JobCategoryCode,
    )
}
```

---

### 3. Data Layer

#### DefaultVacancyRepository.kt

```kotlin
override suspend fun createVacancy(
    title: String,
    description: String,
    salaryMin: Int,
    salaryMax: Int,
    experienceYears: Int,
    employmentType: EmploymentType,
    category: JobCategoryCode
): Result<Vacancy> = runCatching {
    val request = CreateVacancyRequest(
        title = title,
        description = description,
        salaryMin = salaryMin,
        salaryMax = salaryMax,
        minExperienceYears = experienceYears,
        employmentType = employmentType.toResponse(),
        category = category.toResponse(),
        status = VacancyStatusCodeResponse.ACTIVE
    )
    when (val response = dataSource.createVacancy(request)) {
        is NetworkResponse.Success -> response.data.toDomain()
        is NetworkResponse.Error -> throw Exception(response.error)
    }
}
```

#### VacancyMapper.kt

Add mapping functions:
```kotlin
internal fun EmploymentType.toResponse(): EmploymentTypeResponse {
    return EmploymentTypeResponse.valueOf(this.name)
}

internal fun JobCategoryCode.toResponse(): JobCategoryCodeResponse {
    return JobCategoryCodeResponse.valueOf(this.name)
}
```

---

### 4. Presentation Layer

#### CreateVacancyAlert.kt

```kotlin
package not.djinni.presentation.screens.employer.vacancy.create

internal sealed interface CreateVacancyAlert {
    data object SelectEmploymentType : CreateVacancyAlert
    data object SelectCategory : CreateVacancyAlert
}
```

#### CreateVacancyState.kt

```kotlin
package not.djinni.presentation.screens.employer.vacancy.create

import androidx.compose.runtime.Immutable
import not.djinni.model.seeker.vacancy.EmploymentType
import not.djinni.model.seeker.vacancy.JobCategoryCode

@Immutable
internal data class CreateVacancyState(
    val title: String = "",
    val description: String = "",
    val salaryMin: String = "",
    val salaryMax: String = "",
    val experienceYears: String = "",
    val selectedEmploymentType: EmploymentType? = null,
    val selectedCategory: JobCategoryCode? = null,
    val alert: CreateVacancyAlert? = null,
)
```

#### CreateVacancyAction.kt

```kotlin
package not.djinni.presentation.screens.employer.vacancy.create

import not.djinni.model.seeker.vacancy.EmploymentType
import not.djinni.model.seeker.vacancy.JobCategoryCode

internal sealed interface CreateVacancyAction {
    data class UpdateTitle(val value: String) : CreateVacancyAction
    data class UpdateDescription(val value: String) : CreateVacancyAction
    data class UpdateSalaryMin(val value: String) : CreateVacancyAction
    data class UpdateSalaryMax(val value: String) : CreateVacancyAction
    data class UpdateExperienceYears(val value: String) : CreateVacancyAction
    data object ShowEmploymentTypeSheet : CreateVacancyAction
    data object ShowCategorySheet : CreateVacancyAction
    data object HideAlert : CreateVacancyAction
    data class SelectEmploymentType(val type: EmploymentType) : CreateVacancyAction
    data class SelectCategory(val category: JobCategoryCode) : CreateVacancyAction
    data object SubmitVacancy : CreateVacancyAction
}
```

#### CreateVacancySideEffect.kt

```kotlin
package not.djinni.presentation.screens.employer.vacancy.create

internal sealed interface CreateVacancySideEffect {
    data object NavigateBack : CreateVacancySideEffect
    data class NavigateToDetails(val vacancyId: Long) : CreateVacancySideEffect
}
```

#### CreateVacancyScreen.kt

```kotlin
@Composable
internal fun CreateVacancyScreen(
    onNavigateBack: () -> Unit,
    onNavigateToDetails: (Long) -> Unit,
) {
    Screen<CreateVacancyViewModel> { viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()

        Content(
            state = state,
            onAction = viewModel::sendAction
        )

        AlertContainer(state.alert) { alert ->
            when (alert) {
                CreateVacancyAlert.SelectEmploymentType -> {
                    EmploymentTypeBottomSheet(
                        selected = state.selectedEmploymentType,
                        onDismiss = { viewModel.sendAction(CreateVacancyAction.HideAlert) },
                        onSelect = { viewModel.sendAction(CreateVacancyAction.SelectEmploymentType(it)) }
                    )
                }
                CreateVacancyAlert.SelectCategory -> {
                    JobCategoryBottomSheet(
                        selected = state.selectedCategory,
                        onDismiss = { viewModel.sendAction(CreateVacancyAction.HideAlert) },
                        onSelect = { viewModel.sendAction(CreateVacancyAction.SelectCategory(it)) }
                    )
                }
            }
        }

        viewModel.sideEffect.collectAsEffect { effect ->
            when (effect) {
                CreateVacancySideEffect.NavigateBack -> onNavigateBack()
                is CreateVacancySideEffect.NavigateToDetails -> onNavigateToDetails(effect.vacancyId)
            }
        }
    }
}
```

---

### 5. Components

#### EmploymentTypeBottomSheet.kt

Pattern similar to ApplyVacancyBottomSheet:
- ModalBottomSheet wrapper
- LazyColumn with all EmploymentType enum values
- Radio button selection
- On item click → call onSelect and dismiss

#### JobCategoryBottomSheet.kt

Pattern similar to ApplyVacancyBottomSheet:
- ModalBottomSheet wrapper
- LazyColumn with all JobCategoryCode enum values
- Radio button selection
- On item click → call onSelect and dismiss

---

### 6. Navigation

#### EmployerEntry.kt

```kotlin
entry<Screens.Employer.CreateVacancy> {
    CreateVacancyScreen(
        onNavigateBack = { controller.popBackStack() },
        onNavigateToDetails = { vacancyId ->
            controller.navigate(Screens.Employer.VacancyDetails(vacancyId))
        }
    )
}
```

---

## Validation Rules

All fields are required:

| Field | Validation |
|-------|------------|
| Title | Non-empty string |
| Description | Non-empty string |
| Salary Min | >= 0, valid integer |
| Salary Max | > 0, valid integer, must be > salaryMin |
| Experience Years | >= 0, valid integer |
| Employment Type | Must be selected (not null) |
| Category | Must be selected (not null) |
| Status | Always ACTIVE (hardcoded) |

---

## UI Flow

```
┌──────────────────────────────────────┐
│ Create Vacancy                       │
├──────────────────────────────────────┤
│ Title: [________________]            │
│                                      │
│ Description:                         │
│ [_______________________________]    │
│ [_______________________________]    │
│                                      │
│ Salary Min: [_______]                │
│ Salary Max: [_______]                │
│                                      │
│ Experience Years: [_______]          │
│                                      │
│ Employment Type:                     │
│ [  Full-time ▼  ] ← Tappable box    │
│                                      │
│ Category:                            │
│ [  Software Dev ▼  ] ← Tappable     │
│                                      │
│        [ Create Vacancy ]            │
└──────────────────────────────────────┘

On Employment Type tap:
  → ShowEmploymentTypeSheet action
  → alert = SelectEmploymentType
  → EmploymentTypeBottomSheet shown

On Category tap:
  → ShowCategorySheet action
  → alert = SelectCategory
  → JobCategoryBottomSheet shown

On selection in bottom sheet:
  → Select* action
  → Update state, hide alert
  → Sheet dismissed

On Submit:
  → Validate all fields
  → If valid: API call → NavigateToDetails(newId)
  → If invalid: Show error snackbar
```

---

## Implementation Order

1. Network layer (Request DTO, DataSource)
2. Domain layer (Repository interface, Use Case)
3. Data layer (Repository implementation, Mappers)
4. Presentation Alert enum
5. Presentation State/Action/SideEffect updates
6. ViewModel logic
7. Bottom sheet components
8. Screen UI
9. Navigation update

---

## Notes

- Use existing EmploymentType and JobCategoryCode domain enums
- Map to response enums only at repository level
- Always pass VacancyStatusCodeResponse.ACTIVE (not user-selectable)
- Follow AlertContainer pattern from VacancyDetailsScreen
- Use TextFieldState for all text inputs
- Validation errors shown via snackbar
- Success navigates to newly created vacancy details
