# UI Fixes Report

## What Was Done

- Fixed cover letter template apply flow in `CoverLetterTemplatesViewModel`:
  - popup state is closed first
  - a 120 ms delay is applied
  - `ApplyTemplate` is emitted after the popup closes
- Fixed vacancy card company title layout in `VacancyCard`:
  - title now uses layout weight
  - title is limited to one line with ellipsis
  - like button area stays visible and unobstructed
- Styled work experience date picker dialogs in seeker profile create/edit screens:
  - dialogs now use `NotDjinniTheme` colors
- Updated work experience checkbox styling in seeker profile create/edit screens:
  - checked/unchecked colors now use `NotDjinniTheme` `primary`, `onPrimary`, and `onSurface`

## What Files Changed

- `app/src/main/kotlin/not/djinni/presentation/screens/seeker/coverletter/templates/CoverLetterTemplatesViewModel.kt`
- `app/src/main/kotlin/not/djinni/presentation/core/components/base/VacancyCard.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/seeker/profile/create/CreateSeekerProfileScreen.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/seeker/profile/edit/EditSeekerProfileScreen.kt`

## How To Test

### Build And Unit Validation

- Run `./gradlew :app:compileDebugKotlin`
- Expected result: `BUILD SUCCESSFUL`
- Run `./gradlew testDebugUnitTest`
- Expected result: `BUILD SUCCESSFUL`

### Emulator Validation Reference

- Date: May 18, 2026
- Device: Pixel 9 Pro AVD, `emulator-5554`
- Validation note: user confirmed the Edit Profile popup is the acceptable target for date picker validation

### Scenarios

1. Cover template apply flow:
   - Open cover letter templates
   - Tap `Apply` on a template
   - Verify the popup closes before navigation/back handling finishes
   - Verify the selected template is applied after the close transition
2. Vacancy card company title:
   - Open a seeker vacancy list with long company names
   - Verify company title is truncated to one line with ellipsis
   - Verify the like icon stays visible and is not overlapped by text
3. Work experience date picker styling:
   - Open create or edit seeker profile
   - Open work experience date picker
   - Verify dialog colors follow `NotDjinniTheme`
4. Work experience checkbox styling:
   - In create or edit seeker profile, toggle the current-position checkbox
   - Verify checkbox colors match `NotDjinniTheme` `primary`, `onPrimary`, and `onSurface`

### Validation Evidence

- `/tmp/notdjinni-validation/26-template-dialog-before-apply.png`
- `/tmp/notdjinni-validation/27-after-template-apply.png`
- `/tmp/notdjinni-validation/coverapply.mp4`
- `/tmp/notdjinni-validation/02-seeker-main.png`
- `/tmp/notdjinni-validation/12b-date-picker-edit.png`
- `/tmp/notdjinni-validation/11b-edit-workexp-dialog.png`
- `/tmp/notdjinni-validation/14-checkbox-checked-edit.png`
- `/tmp/notdjinni-validation/logcat.txt`

### Passed Validation Summary

- `./gradlew :app:compileDebugKotlin` -> `PASS`
- `./gradlew testDebugUnitTest` -> `PASS`
- Pixel 9 Pro emulator validation -> `PASS`
