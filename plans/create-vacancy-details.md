# Vacancy Details Implementation Plan

**Created:** 2025-12-01  
**Status:** Ready for implementation

---

## Overview

Implement two distinct Vacancy Details screens:
- **Seeker VacancyDetails** - With eligibility indicators and Apply button
- **Employer VacancyDetails** - Pure vacancy information display

---

## Eligibility Rules

| Criteria | Rule | UI Behavior |
|----------|------|-------------|
| Experience | `seeker.experienceYears >= vacancy.minExperienceYears` | Mismatch → `error` color, disable Apply |
| Salary | `vacancy.salaryMax >= seeker.desiredSalary` | Mismatch → `primary` highlight + hint |
| Job Category | Not checked | - |
| Apply Button | Disabled if experience mismatch | Replace with "Cannot apply" text |

---

## Files to Modify/Create

### Layer 1: Network

| File | Action |
|------|--------|
| `network/vacancy/VacancyDataSource.kt` | Add `getVacancyById(id: Long)` |
| `network/vacancy/DefaultVacancyDataSource.kt` | Implement method |
| `network/vacancy/resource/Vacancy.kt` | Add `ById` nested resource |

### Layer 2: Repository

| File | Action |
|------|--------|
| `domain/repository/VacancyRepository.kt` | Add `getVacancyById(id: Long)` |
| `data/repository/DefaultVacancyRepository.kt` | Implement method |

### Layer 3: Theme

| File | Action |
|------|--------|
| `theme/icons/CheckIcon.kt` | Create new icon |
| `theme/icons/CloseIcon.kt` | Create new icon |
| `theme/NotDjinniIcons.kt` | Add `check`, `close` references |

### Layer 4: Core Extensions

| File | Action |
|------|--------|
| `core/extension/Instant.kt` | Add `toFormattedFullDate()` → "Jan 15, 2025" |

### Layer 5: Seeker VacancyDetails

| File | Action |
|------|--------|
| `screens/seeker/vacancy/details/VacancyDetailsState.kt` | Sealed `ContentState` with `Loading`, `Error`, `Data` |
| `screens/seeker/vacancy/details/VacancyDetailsAction.kt` | `Retry`, `Apply`, `NavigateBack` |
| `screens/seeker/vacancy/details/VacancyDetailsSideEffect.kt` | `NavigateBack`, `NavigateToApply` |
| `screens/seeker/vacancy/details/VacancyDetailsViewModel.kt` | Load vacancy + profile, calculate eligibility |
| `screens/seeker/vacancy/details/VacancyDetailsScreen.kt` | Full UI with eligibility section |

### Layer 6: Employer VacancyDetails

| File | Action |
|------|--------|
| `screens/employer/vacancy/details/VacancyDetailsState.kt` | Sealed `ContentState` (no eligibility) |
| `screens/employer/vacancy/details/VacancyDetailsAction.kt` | `Retry`, `NavigateBack` |
| `screens/employer/vacancy/details/VacancyDetailsSideEffect.kt` | `NavigateBack` |
| `screens/employer/vacancy/details/VacancyDetailsViewModel.kt` | Load vacancy only |
| `screens/employer/vacancy/details/VacancyDetailsScreen.kt` | Pure details UI |

### Layer 7: Navigation

| File | Action |
|------|--------|
| `navigation/controller/SeekerEntry.kt` | Add `onNavigateBack`, `onApply` callbacks |
| `navigation/controller/EmployerEntry.kt` | Add `onNavigateBack` callback |

### Layer 8: Resources

| File | Action |
|------|--------|
| `res/values/strings.xml` | Add new strings (see below) |

---

## State Structure

### Seeker

```kotlin
@Immutable
internal data class VacancyDetailsState(
    val contentState: VacancyDetailsContentState = VacancyDetailsContentState.Loading,
)

internal sealed interface VacancyDetailsContentState {
    data object Loading : VacancyDetailsContentState
    data class Error(val message: TextData) : VacancyDetailsContentState
    data class Data(
        val vacancy: VacancyDisplayData,
        val eligibility: EligibilityState?
    ) : VacancyDetailsContentState
}

@Immutable
internal data class VacancyDisplayData(
    val id: Long,
    val title: TextData,
    val companyName: TextData,
    val companyDescription: TextData?,
    val description: TextData,
    val salaryRange: TextData,
    val employmentType: TextData,
    val requiredExperience: TextData,
    val category: TextData?,
    val postedDate: TextData,
)

@Immutable
internal data class EligibilityState(
    val canApply: Boolean,
    val experienceMatch: Boolean,
    val salaryMatch: Boolean,
    val salaryHint: TextData?,
)
```

### Employer

```kotlin
@Immutable
internal data class VacancyDetailsState(
    val contentState: VacancyDetailsContentState = VacancyDetailsContentState.Loading,
)

internal sealed interface VacancyDetailsContentState {
    data object Loading : VacancyDetailsContentState
    data class Error(val message: TextData) : VacancyDetailsContentState
    data class Data(val vacancy: VacancyDisplayData) : VacancyDetailsContentState
}

@Immutable
internal data class VacancyDisplayData(
    val id: Long,
    val title: TextData,
    val description: TextData,
    val salaryRange: TextData,
    val employmentType: TextData,
    val requiredExperience: TextData,
    val category: TextData?,
    val status: TextData,
    val postedDate: TextData,
    val updatedDate: TextData,
)
```

---

## String Resources

```xml
<!-- Vacancy Details -->
<string name="vacancy_details_title">Vacancy Details</string>
<string name="vacancy_details_apply">Apply Now</string>
<string name="vacancy_details_description">Description</string>
<string name="vacancy_details_about_company">About Company</string>
<string name="vacancy_details_posted">Posted: %s</string>
<string name="vacancy_details_updated">Last updated: %s</string>
<string name="vacancy_details_error">Failed to load vacancy details</string>
<string name="vacancy_cannot_apply">You don\'t meet the experience requirements</string>
<string name="retry">Retry</string>

<!-- Eligibility -->
<string name="eligibility_experience_match">Your experience matches the requirements</string>
<string name="eligibility_experience_mismatch">Vacancy requires more experience than you have</string>
<string name="vacancy_salary_below_hint">Salary is below your expectations ($%d)</string>

<!-- Vacancy Status -->
<string name="vacancy_status_draft">Draft</string>
<string name="vacancy_status_active">Active</string>
<string name="vacancy_status_paused">Paused</string>
<string name="vacancy_status_closed">Closed</string>
<string name="vacancy_status_expired">Expired</string>
```

---

## UI Layout

### Seeker Screen

```
┌─────────────────────────────────┐
│ ← Vacancy Details               │
├─────────────────────────────────┤
│ Company Name                    │
│ VACANCY TITLE (large)           │
│ $X,XXX - $X,XXX | Full-time     │
│ X+ years experience             │
├─────────────────────────────────┤
│ ✓ Your experience matches       │
│   (or ✗ with error color)       │
├─────────────────────────────────┤
│ Description                     │
│ [Long text content...]          │
├─────────────────────────────────┤
│ About Company                   │
│ [Company description...]        │
├─────────────────────────────────┤
│       [  Apply Now  ]           │
│  (or "Cannot apply" text)       │
└─────────────────────────────────┘
```

### Employer Screen

```
┌─────────────────────────────────┐
│ ← Vacancy Details               │
├─────────────────────────────────┤
│ VACANCY TITLE (large)           │
│ Status: Active                  │
│ $X,XXX - $X,XXX | Full-time     │
│ X+ years experience             │
├─────────────────────────────────┤
│ Description                     │
│ [Long text content...]          │
├─────────────────────────────────┤
│ Posted: Jan 15, 2025            │
│ Last updated: Jan 20, 2025      │
└─────────────────────────────────┘
```

---

## Implementation Order

1. Network + Repository layer
2. Theme (icons + date extension)
3. Employer VacancyDetails (simpler)
4. Seeker VacancyDetails (with eligibility)
5. Navigation updates
6. String resources

---

## Notes

- Package mismatch in existing files needs fixing (e.g., `seeker/vacancydetails` → `seeker/vacancy/details`)
- If seeker profile is null, hide eligibility section completely
- Use `controller::popBackStack` for back navigation
- Salary mismatch uses `primary` color (yellow/gold) for highlighting
- Experience mismatch uses `error` color (red)
