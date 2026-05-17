# Public Vacancy Browsing Report

## What was done

- Added public vacancy browsing entry via `Screens.Public`.
- Added unauthenticated public vacancy list and public vacancy details flow.
- Removed seeker-only UI/actions from public flow: Recommended, Profile, Applications, Apply, and See application.
- Added auth return from public browsing with cleared back stack.
- Kept demo seeker login flow working after public browsing.

## What files changed

- `AGENTS.md`
- `ai/report/add-demo-users-doc-report.md`
- `ai/specs/public-vacancy-browsing-spec.md`
- `app/src/main/kotlin/not/djinni/data/repository/DefaultVacancyRepository.kt`
- `app/src/main/kotlin/not/djinni/domain/repository/VacancyRepository.kt`
- `app/src/main/kotlin/not/djinni/network/vacancy/DefaultVacancyDataSource.kt`
- `app/src/main/kotlin/not/djinni/network/vacancy/VacancyDataSource.kt`
- `app/src/main/kotlin/not/djinni/presentation/navigation/NotDjinniNavDisplay.kt`
- `app/src/main/kotlin/not/djinni/presentation/navigation/controller/AuthEntry.kt`
- `app/src/main/kotlin/not/djinni/presentation/navigation/controller/PublicEntry.kt`
- `app/src/main/kotlin/not/djinni/presentation/navigation/controller/Screens.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/auth/AuthAction.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/auth/AuthScreen.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/auth/AuthSideEffect.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/auth/AuthViewModel.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/public/main/PublicMainAction.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/public/main/PublicMainScreen.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/public/main/PublicMainSideEffect.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/public/main/PublicMainState.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/public/main/PublicMainViewModel.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/public/vacancy/details/PublicVacancyDetailsAction.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/public/vacancy/details/PublicVacancyDetailsScreen.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/public/vacancy/details/PublicVacancyDetailsSideEffect.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/public/vacancy/details/PublicVacancyDetailsState.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/public/vacancy/details/PublicVacancyDetailsViewModel.kt`
- `app/src/main/res/values/strings.xml`
- `docs/demo-users.md`

## How to test

- Run `./gradlew :app:compileDebugKotlin -q`.
- Run `./gradlew :app:testDebugUnitTest -q`.
- On emulator, open Auth and tap `View only`.
- Verify public list opens with cleared back stack and without Recommended, Profile, or Applications.
- Open public vacancy details and verify Apply and See application are absent.
- Tap login icon and verify Auth opens with cleared back stack.
- Log in with demo seeker from `docs/demo-users.md`.
- Confirm crash buffer is empty.
