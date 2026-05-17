# Vacancy Views Count Report

## What was done
- Added `views_count` support to the vacancy details response and mapped it through the seeker vacancy domain model.
- Added vacancy views count display to seeker `Vacancy Details`.
- Placed the views count with an eye icon at the bottom of the scrollable vacancy text content, before the fixed apply section.
- Added JVM coverage for vacancy details response-to-domain `viewsCount` mapping.

## Files changed
- `app/src/main/kotlin/not/djinni/network/vacancy/response/VacancyDetailsResponse.kt`
- `app/src/main/kotlin/not/djinni/model/seeker/vacancy/Vacancy.kt`
- `app/src/main/kotlin/not/djinni/model/seeker/vacancy/VacancyMapper.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/seeker/vacancy/details/VacancyDetailsState.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/seeker/vacancy/details/VacancyDetailsViewModel.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/seeker/vacancy/details/VacancyDetailsScreen.kt`
- `app/src/test/kotlin/not/djinni/data/repository/DefaultApplicationRepositoryTest.kt`
- `app/src/test/kotlin/not/djinni/model/seeker/vacancy/VacancyMapperTest.kt`

## How to test / Validation
- Run mapper-focused unit test:
  - `./gradlew testDebugUnitTest --tests not.djinni.model.seeker.vacancy.VacancyMapperTest`
- Run full debug unit test suite:
  - `./gradlew testDebugUnitTest`
- Unit validation passed.
- Pixel 9 Pro Emulator QA passed:
  - Device: `emulator-5554`, `Pixel_9_Pro`, `RUNNING_UNLOCKED`
  - App: `not.djinni` debug `not-djinni-debug-0.0.2-2.apk`
  - Steps: installed and launched app, selected seeker flow, opened Vacancies, opened two vacancy details screens.
  - Evidence: vacancy list loads; list response includes `views_count`; details `/vacancy/49` and `/vacancy/50` returned `200 OK` with `views_count: 2`; no `JsonConvertException` or fatal crash; UI tree shows views count text `2` in scroll content near bottom, not in top bar or top-right; eye icon visible in screenshot but not exposed in UI tree.
  - Note: long-scroll case was not exercised because tested vacancies had short descriptions.
