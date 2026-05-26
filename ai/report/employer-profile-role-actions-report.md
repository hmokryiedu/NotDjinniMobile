# Employer Profile Role Actions Report

## What was done

- Fixed employer profile role flow bug.
- Added `Edit Role` action on employer profile screen.
- Kept `Choose Role` action.
- `Edit Role` now changes role and updates employer profile.
- `Choose Role` now navigates to choose-role screen.

## Files changed

- `app/src/main/kotlin/not/djinni/presentation/screens/employer/profile/my/EmployerProfileScreen.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/employer/profile/my/EmployerProfileAction.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/employer/profile/my/EmployerProfileSideEffect.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/employer/profile/my/EmployerProfileViewModel.kt`
- `app/src/main/kotlin/not/djinni/presentation/navigation/controller/EmployerEntry.kt`
- `app/src/main/res/values/strings.xml`

## How to test

### Unit test

- Run: `./gradlew :app:testDebugUnitTest --console=plain`
- Expected: PASS

### Emulator QA

- Device: `emulator-5554`
- User: `employer.097@notdjinni.local`
- Password: `Employer097Pass!`

#### Edit Role flow

- Open employer profile.
- Verify initial role/company: `Talent Acquisition Specialist / FRACTAL`
- Tap `Edit Role`.
- Change role to `QA52`
- Save.
- Verify profile shows: `QA52 / FRACTAL`

#### Choose Role flow

- From employer profile, tap `Choose Role`.
- Verify target screen contains text: `What’s your role?`

### Validation artifacts

- Screenshot: `/private/tmp/notdjinni-validation-20260526/step8-profile.png`
- Screenshot: `/private/tmp/notdjinni-validation-20260526/step14-choose-role.png`
- UI dump: `/private/tmp/notdjinni-validation-20260526/step13-after-save.xml`

### Notes

- Emulator text injection flaky for longer strings.
- Approved validation flow passed.
