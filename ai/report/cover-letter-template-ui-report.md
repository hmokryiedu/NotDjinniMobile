# Cover Letter Template UI Report

## What was done

- Styled delete confirmation popup for cover letter templates, including spacing between descriptive text and input field content.
- Added delete icon on template cards and wired delete flow so delete action opens confirmation popup only.
- Moved card trailing arrow to row end as `text  >`.
- Placed delete icon at card top-right.
- Vertically centered card main content, including template text and trailing arrow, while keeping delete action separate.
- Added edit dialog text field auto-focus and `imePadding` so input stays usable with visible keyboard.

## What files changed

- `app/src/main/kotlin/not/djinni/presentation/screens/seeker/coverletter/templates/CoverLetterTemplatesScreen.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/seeker/coverletter/templates/CoverLetterTemplatesAction.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/seeker/coverletter/templates/CoverLetterTemplatesViewModel.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/seeker/coverletter/templates/CoverLetterTemplatesState.kt`
- `ai/report/cover-letter-template-ui-report.md`

## How to test

- Run unit validation:
  - `./gradlew :app:testDebugUnitTest --console=plain`
  - Expect: `BUILD SUCCESSFUL`
- Install fresh build:
  - `./gradlew :app:installDebug --console=plain --quiet`
- Open app on emulator `emulator-5554`, package/activity `not.djinni/.presentation.MainActivity`.
- Sign in with demo seeker user `seeker.001@notdjinni.local`.
- Open cover letter templates screen.
- Verify template card shows template text, trailing `>`, delete icon in top-right, text/arrow vertically centered, open and delete hit areas do not overlap.
- Tap delete icon. Verify styled confirmation popup opens. Tap cancel. Verify template remains.
- Tap card body/text area. Verify template edit popup opens, text field auto-focuses, keyboard becomes visible, `imePadding` keeps field usable.
- Confirm delete in popup. Verify selected template is removed.
- Validation note: validated run deleted existing demo seeker template, final screen state was `No templates yet`.
