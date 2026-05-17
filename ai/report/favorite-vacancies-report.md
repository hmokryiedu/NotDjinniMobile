# Favorite Vacancies Report

## What Was Done

- Added Favorite Vacancies flow for seekers.
- Added top bar favorite entry point that opens Favorite Vacancies.
- Added favorite/unfavorite support from vacancy list cards.
- Added Favorite Vacancies screen with filled and empty states.
- Added favorite toggle support in vacancy details.
- Fixed stale favorite screen state after unfavoriting from details and immediately pressing Back.
- Ensured filled heart tint is visible on favorite vacancy cards.

## Files Changed

- `app/src/main/kotlin/not/djinni/data/repository/DefaultVacancyRepository.kt`
- `app/src/main/kotlin/not/djinni/domain/repository/VacancyRepository.kt`
- `app/src/main/kotlin/not/djinni/model/seeker/vacancy/Vacancy.kt`
- `app/src/main/kotlin/not/djinni/model/seeker/vacancy/VacancyMapper.kt`
- `app/src/main/kotlin/not/djinni/network/vacancy/DefaultVacancyDataSource.kt`
- `app/src/main/kotlin/not/djinni/network/vacancy/VacancyDataSource.kt`
- `app/src/main/kotlin/not/djinni/network/vacancy/resource/FavoriteVacancy.kt`
- `app/src/main/kotlin/not/djinni/network/vacancy/response/VacancyDetailsResponse.kt`
- `app/src/main/kotlin/not/djinni/presentation/core/components/base/VacancyCard.kt`
- `app/src/main/kotlin/not/djinni/presentation/core/components/base/model/VacancyCardData.kt`
- `app/src/main/kotlin/not/djinni/presentation/navigation/controller/Screens.kt`
- `app/src/main/kotlin/not/djinni/presentation/navigation/controller/SeekerEntry.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/employer/main/MainEmployerViewModel.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/seeker/main/MainSeekerAction.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/seeker/main/MainSeekerScreen.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/seeker/main/MainSeekerSideEffect.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/seeker/main/MainSeekerViewModel.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/seeker/vacancy/applied/AppliedVacanciesViewModel.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/seeker/vacancy/details/VacancyDetailsAction.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/seeker/vacancy/details/VacancyDetailsScreen.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/seeker/vacancy/details/VacancyDetailsState.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/seeker/vacancy/details/VacancyDetailsViewModel.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/seeker/vacancy/favorite/`
- `app/src/main/res/values/strings.xml`

Pre-existing dirty or untracked state left untouched:

- `AGENTS.md`
- `.codex/skills/plain-task-doc-writer/`
- `ai/specs/favorite-vacancies-spec.md`

## Validation Evidence

- Unit/build command passed: `./gradlew :app:testDebugUnitTest :app:compileDebugKotlin --console=plain`
- Result: `BUILD SUCCESSFUL in 4s`
- Emulator: `emulator-5554`
- Demo user: `demo-202605131244-07@notdjinni.test`, Sofia Danylchuk
- QA passed:
  - Top bar favorite opens Favorite Vacancies.
  - Add/remove favorite works from vacancy list.
  - Favorite Vacancies screen shows favorite vacancies and empty state correctly.
  - Details top bar favorite toggles favorite state.
  - Stale-state fix verified: favorited `Withdraw Test withdraw20260513204613`, opened Favorite Vacancies, opened details, unfavorited, pressed Back immediately, and Favorite Vacancies showed `No favorite vacancies yet`.
  - Filled heart tint visible on `Uklon / Viewed Jobs Backend Engineer`.

Caveat: ADB needed escalated run after sandbox blocked daemon restart, but validation passed.

## How To Test

1. Run `./gradlew :app:testDebugUnitTest :app:compileDebugKotlin --console=plain`.
2. Launch app on `emulator-5554`.
3. Sign in as `demo-202605131244-07@notdjinni.test` Sofia Danylchuk.
4. Open Favorite Vacancies from seeker top bar.
5. Favorite and unfavorite vacancies from list and details.
6. Confirm Favorite Vacancies updates correctly, including empty state after immediate Back from details.
