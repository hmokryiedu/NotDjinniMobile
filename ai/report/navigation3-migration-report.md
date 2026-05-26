# Navigation3 Migration Report

## What Was Done

- Migrated app navigation to the approved Navigation3 model with multi-parent/group stacks and dynamic root/current parent handling.
- Moved `ChooseRole` into the Auth group.
- Updated navigation rendering so `displayStack` is passed into `NavDisplay`.
- Reworked `NavigationController` behavior for dynamic root/current parent, `navigate`, `pop`, `replace`, and `popUpTo`.
- Switched result passing to the official Navigation3 result bus decorator.
- Restored the cover-letter result flow using a scoped result key.
- Removed the custom navigation result implementation and old result contract files.
- Updated project navigation rules in `ai/rules/navigation3.md`.
- Fixed the Koin KSP compiler version in `gradle/libs.versions.toml` from `4.2.1` to `2.3.1` to resolve a nonexistent compiler artifact.

## What Files Changed

- `gradle/libs.versions.toml`
- `app/src/main/kotlin/not/djinni/presentation/MainActivity.kt`
- `app/src/main/kotlin/not/djinni/presentation/navigation/NavigationController.kt`
- `app/src/main/kotlin/not/djinni/presentation/navigation/NotDjinniNavDisplay.kt`
- `app/src/main/kotlin/not/djinni/presentation/navigation/controller/Screens.kt`
- `app/src/main/kotlin/not/djinni/presentation/navigation/controller/SeekerEntry.kt`
- `app/src/main/kotlin/not/djinni/presentation/navigation/controller/PublicEntry.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/seeker/coverletter/templates/CoverLetterTemplatesScreen.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/seeker/profile/view/SeekerProfileScreen.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/seeker/profile/edit/EditSeekerProfileScreen.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/seeker/vacancy/details/VacancyDetailsScreen.kt`
- `app/src/test/kotlin/not/djinni/presentation/navigation/NavigationControllerTest.kt`
- `ai/rules/navigation3.md`

### Deleted

- `app/src/main/kotlin/not/djinni/presentation/navigation/NavResult.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/seeker/profile/edit/ProfileEditResultContract.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/seeker/vacancy/details/coverletter/CoverLetterResultContract.kt`

## How To Test

### Unit Validation

- Run `./gradlew :app:testDebugUnitTest -q`
- Expected result: pass

### Emulator Validation Summary

- Device: Pixel 9 Pro AVD, `emulator-5554`
- Passed scenarios:
  - build, install, and launch
  - public vacancy list -> vacancy details -> back -> root back exits
  - auth choose-role back chain: role picker -> auth -> launcher
  - seeker login, vacancy apply sheet, cover-letter templates, apply result shown in field
  - seeker profile edit, save, back, and refresh
  - seeker root back exits
  - employer login and root flow
  - employer vacancy details -> applications -> application details -> back chain
  - employer create vacancy -> details via `popUpTo` -> one back returns to My Vacancies
  - employer root back exits

### Validation Notes

- Rotation/config-change validation was blocked by the emulator. Settings were changed, but the display stayed portrait and dumps still reported orientation/rotation `0`.
- Rotation evidence: `/tmp/notdjinni_validation_item3_*`
- Additional evidence:
  - `/tmp/notdjinni_15_apply_sheet_with_template.png`
  - `/tmp/notdjinni_ui_15_apply_sheet_with_template.xml`
  - `/tmp/notdjinni_validation_item1_application_details.png`
  - `/tmp/notdjinni_validation_item2_my_vacancies_after_back.png`
  - `/tmp/notdjinni_validation_item3_display_after_rotation.txt`
