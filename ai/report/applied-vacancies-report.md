# Applied Vacancies Report

## What Was Done

- Replaced seeker Applications entry with the Applied Vacancies flow.
- Added client support for `GET /vacancy/applied`.
- Added client support for `GET /application/vacancy/{vacancyId}/mine`.
- Built the Applied Vacancies screen with loading, empty, error, list states, and vacancy navigation.
- Updated vacancy details so already-applied vacancies show `See Application`, hide `Apply Now`, fetch the current user's application by vacancy id, and navigate to existing Application Details.
- Added focused unit coverage for application repository mine-by-vacancy behavior.
- Added minimal JUnit test dependency.

## Files Changed

- `app/build.gradle.kts`
- `gradle/libs.versions.toml`
- `app/src/main/kotlin/not/djinni/network/vacancy/resource/Vacancy.kt`
- `app/src/main/kotlin/not/djinni/network/vacancy/VacancyDataSource.kt`
- `app/src/main/kotlin/not/djinni/network/vacancy/DefaultVacancyDataSource.kt`
- `app/src/main/kotlin/not/djinni/domain/repository/VacancyRepository.kt`
- `app/src/main/kotlin/not/djinni/data/repository/DefaultVacancyRepository.kt`
- `app/src/main/kotlin/not/djinni/network/application/resource/Application.kt`
- `app/src/main/kotlin/not/djinni/network/application/ApplicationDataSource.kt`
- `app/src/main/kotlin/not/djinni/network/application/DefaultApplicationDataSource.kt`
- `app/src/main/kotlin/not/djinni/domain/repository/ApplicationRepository.kt`
- `app/src/main/kotlin/not/djinni/data/repository/DefaultApplicationRepository.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/seeker/vacancy/applied/*`
- `app/src/main/kotlin/not/djinni/presentation/navigation/controller/SeekerEntry.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/seeker/vacancy/details/*`
- `app/src/main/res/values/strings.xml`
- `app/src/test/kotlin/not/djinni/data/repository/DefaultApplicationRepositoryTest.kt`

## Validation Evidence

- Device: Pixel_9_Pro, serial emulator-5554.
- User: seeker `demo-202605131244-06@notdjinni.test`.
- QA passed:
  - Login passed against `192.168.0.81:8080`.
  - Seeker main opened: UI tree showed `Vacancies`.
  - Applications icon opened `Applied Vacancies`.
  - Applied list visible: `Uklon / Java API Engineer`.
  - Vacancy details opened: UI tree showed `See Application`, no `Apply Now`.
  - `See Application` opened `Application Details`, status `Applied`.
  - Non-applied vacancy check passed: `Withdraw Test... / Backend Validation Engineer` showed `Apply Now`.
  - Crash log buffer empty.
- Commands passed:
  - `./gradlew :app:testDebugUnitTest --tests not.djinni.data.repository.DefaultApplicationRepositoryTest --console=plain`
  - `./gradlew :app:testDebugUnitTest --console=plain`
  - `./gradlew :app:compileDebugKotlin --console=plain`

## How To Test

1. Run `./gradlew :app:testDebugUnitTest --console=plain`.
2. Run `./gradlew :app:compileDebugKotlin --console=plain`.
3. On emulator, log in as a seeker, open the Applications/Applied Vacancies entry, open an applied vacancy, verify `See Application` opens Application Details with status `Applied`.
4. Open a vacancy not applied by the user and verify `Apply Now` is still shown.

## Known Unrelated Dirty State

Visible pre-existing dirty deletions were present under `ai/rules` and were not changed:

- `ai/rules/android-architecture-rules.md`
- `ai/rules/android-implementation-rules.md`
- `ai/rules/android-testing-rules.md`
- `ai/rules/android-ui-rules.md`
