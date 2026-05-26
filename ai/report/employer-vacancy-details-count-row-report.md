# Employer Vacancy Details Count Row Report

## What was done
- Added employer vacancy details count row for `views_count` and `applications_count`.
- Extended `VacancyDisplayData` with `viewsCount: TextData` and `applicationsCount: TextData`.
- Mapped `Vacancy.viewsCount` and `Vacancy.applicationsCount` to display `TextData` in `VacancyDetailsViewModel`.
- Updated employer vacancy details UI to show seeker-style count row after `DateInfoSection`.
- Updated preview sample for vacancy details screen.

## Files changed
- `app/src/main/kotlin/not/djinni/presentation/screens/employer/vacancy/details/VacancyDetailsState.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/employer/vacancy/details/VacancyDetailsViewModel.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/employer/vacancy/details/VacancyDetailsScreen.kt`

## How to test
- Run unit validation:
  - `./gradlew app:testDebugUnitTest --tests 'not.djinni.model.seeker.vacancy.VacancyMapperTest' --tests 'not.djinni.data.repository.DefaultVacancyRepositoryTest' --tests 'not.djinni.presentation.navigation.NavigationControllerTest' --console=plain`
- Expected result: `BUILD SUCCESSFUL`.
- Emulator QA:
  - Device: `emulator-5554`
  - Flow: log in with demo employer profile, open `EPAM Systems` vacancy details.
  - Verify `Posted` / `Updated` section stays visible.
  - Verify count row appears below it with `0` views and `0` applications.
  - Tap `View Applications` and verify navigation to Applications screen with no crash.
