# Application Actions Improvements Report

## What was done

- Added applied vacancies status filtering end to end: backend query support for `application_status`, filter trigger above list, draft-based popup apply-on-close behavior, selected-status chips, and chip removal reload behavior.
- Added confirmation dialogs for application withdraw and logout, so destructive actions run only after explicit user confirmation.
- Fixed cover letter template apply flow behavior for task `NOT-52`.
- Kept seeker profile create/edit UI aligned for work-experience inputs; validation confirmed `CreateSeekerProfile` top alignment stayed correct.
- Validation passed with unit/build checks and device QA on SM-A346E.

## What files changed

- Implementation summary touched these areas:
  - `app/src/main/kotlin/not/djinni/network/{application,vacancy}/**`
  - `app/src/main/kotlin/not/djinni/data/repository/**`
  - `app/src/main/kotlin/not/djinni/domain/repository/**`
  - `app/src/main/kotlin/not/djinni/presentation/navigation/controller/SeekerEntry.kt`
  - `app/src/main/kotlin/not/djinni/presentation/screens/seeker/vacancy/applied/**`
  - `app/src/main/kotlin/not/djinni/presentation/screens/seeker/application/details/**`
  - `app/src/main/kotlin/not/djinni/presentation/screens/seeker/coverletter/templates/**`
  - `app/src/main/kotlin/not/djinni/presentation/screens/seeker/profile/{create,edit}/**`
  - `app/src/main/kotlin/not/djinni/presentation/screens/seeker/{main,profile}/**`
  - `app/src/main/res/values/strings.xml`
  - targeted unit tests under `app/src/test/kotlin/**`

## How to test

1. Run `./gradlew :app:testDebugUnitTest --console=plain` and confirm pass.
2. Run `./gradlew :app:installDebug --console=plain` and confirm pass on SM-A346E.
3. On device `SM-A346E` (`adb-RFCX2036J3V-0EdI3I._adb-tls-connect._tcp`), sign in with `seeker.013@notdjinni.local / Seeker013Pass!`.
4. Open Applied Vacancies, open status filter, select multiple statuses, close popup, verify single reload only after close. Evidence:
   - popup open: `/private/tmp/notdjinni-validation-20260523/13-filter-popup-open.png`
   - chips visible: `/private/tmp/notdjinni-validation-20260523/17-chips-visible.png`
   - chip removal works: `/private/tmp/notdjinni-validation-20260523/18-chip-remove-works.png`
5. Open application details, trigger withdraw, verify confirmation dialog appears, cancel keeps state, confirm performs withdraw.
6. Open cover letter templates, verify template apply flow matches expected fixed behavior from `NOT-52`.
7. Open `CreateSeekerProfile`, verify work-experience input alignment stays correct; measured top Y for both inputs = `767`. Evidence screenshot: `/private/tmp/create_seeker_profile_alignment_sm-a346e.png`.
8. Trigger logout, verify confirmation dialog appears and quick-check passes.

Validation evidence:
- `./gradlew :app:testDebugUnitTest --console=plain` passed.
- `./gradlew :app:installDebug --console=plain` passed on SM-A346E.
- AppliedVacancies UI passed on SM-A346E serial `adb-RFCX2036J3V-0EdI3I._adb-tls-connect._tcp`.
- `CreateSeekerProfile` alignment passed on SM-A346E with both top Y values `767`.
- Logout confirm quick-check passed.
