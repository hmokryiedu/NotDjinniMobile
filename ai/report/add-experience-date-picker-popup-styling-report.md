# Add Experience Date Picker Popup Styling Report

## What Was Done

- Fixed Add Experience date picker popup input error styling in seeker profile create and edit flows.
- Resolved the invalid End date label rendering issue where the popup showed a black rectangular cutout and broken border overlap.
- Root cause: `DateRangePicker` input fields internally use `OutlinedTextField`, but `dateTextFieldColors` was configured with filled `TextFieldDefaults` colors.
- Replaced `TextFieldDefaults` with `OutlinedTextFieldDefaults` for `dateTextFieldColors`.
- Added explicit error text, border, label, supporting, and cursor colors from `NotDjinniTheme`.
- Removed filled container overrides that conflicted with outlined input rendering.

## What Files Changed

- `app/src/main/kotlin/not/djinni/presentation/screens/seeker/profile/create/CreateSeekerProfileScreen.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/seeker/profile/edit/EditSeekerProfileScreen.kt`

## How To Test

1. Run `./gradlew testDebugUnitTest` and verify `BUILD SUCCESSFUL`.
2. Launch Pixel 10 Pro emulator.
3. Sign in with a seeker demo user from `docs/demo-users.md`.
4. Open `Profile -> Edit -> Add work experience -> Date range`.
5. Type invalid End date `02/42/026`.
6. Verify the invalid End date field keeps a clean outlined error state with no black rectangular cutout and no broken border/label overlap.

## Validation Evidence

- `./gradlew testDebugUnitTest` -> `BUILD SUCCESSFUL`
- Pixel 10 Pro emulator QA passed with invalid End date `02/42/026` on `Profile -> Edit -> Add work experience -> Date range`
- Screenshot: `/private/tmp/notdjinni-validation/step7c-invalid-typed.png`
- UI dump: `/private/tmp/notdjinni-validation/step7c-invalid-typed.xml`
