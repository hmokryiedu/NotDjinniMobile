# Applied Vacancy Details Navigation Report

## What Was Done

- Fixed applied vacancies click flow.
- Changed applied vacancy navigation target from Application Details to `VacancyDetailsScreen`.
- Updated applied vacancies action, side effect, state, view model, screen, and seeker navigation wiring to use vacancy-details route.

## Files Changed

- `app/src/main/kotlin/not/djinni/presentation/screens/seeker/vacancy/applied/AppliedVacanciesAction.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/seeker/vacancy/applied/AppliedVacanciesSideEffect.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/seeker/vacancy/applied/AppliedVacanciesViewModel.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/seeker/vacancy/applied/AppliedVacanciesScreen.kt`
- `app/src/main/kotlin/not/djinni/presentation/navigation/controller/SeekerEntry.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/seeker/vacancy/applied/AppliedVacanciesState.kt`

## How To Test

1. Run `./gradlew :app:testDebugUnitTest`.
2. Open seeker applied vacancies flow.
3. Tap any applied vacancy.
4. Verify app opens `Screens.Seeker.VacancyDetails(vacancyId)`, not Application Details.
