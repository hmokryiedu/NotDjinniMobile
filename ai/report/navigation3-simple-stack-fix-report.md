# Navigation3 Simple Stack Fix Report

## What was done

- Simplified Navigation3 to one active `NavBackStack<Screens>` in `NavigationController`.
- Kept standard child navigation with `navigate`, exact stack replacement with `replaceAll`, root-safe back handling, and missing-anchor-safe `popUpTo`.
- Updated `NavDisplay` to render `controller.stack` while keeping the current decorators and transitions.
- Removed `parentRoute` from `Screens` and kept typed routes, including `CoverLetterTemplates(resultKey)`.
- Adjusted auth success flow so `navigate(Screens.ChooseRole)` preserves Back to Auth.
- Rewrote navigation unit tests for the single-stack behavior.
- Updated the core navigation rule and refreshed stale Navigation3 docs to match the implemented model.

## What files changed

- `app/src/main/kotlin/not/djinni/presentation/navigation/NavigationController.kt`
- `app/src/main/kotlin/not/djinni/presentation/navigation/NotDjinniNavDisplay.kt`
- `app/src/main/kotlin/not/djinni/presentation/navigation/controller/Screens.kt`
- `app/src/main/kotlin/not/djinni/presentation/navigation/controller/AuthEntry.kt`
- `app/src/test/kotlin/not/djinni/presentation/navigation/NavigationControllerTest.kt`
- `ai/rules/navigation3.md`
- `docs/navigation3-integration.md`
- `docs/navigation3-integration-guide.md`

## How to test

- Run `./gradlew :app:testDebugUnitTest`
- Confirm unit tests pass for:
  - exact `replaceAll`
  - root `popBackStack`
  - child back pop
  - logout/history clearing
  - `popUpTo` success and missing-anchor behavior
- Emulator QA passed on `emulator-5554` (`Pixel_9_Pro`, `sdk_gphone64_arm64`) for:
  - valid token -> `ChooseRole` -> Back exits app
  - auth seeker login (`demo-202605131244-07@notdjinni.test / User07Aa`) -> `ChooseRole` -> Back returns Auth
  - seeker vacancy -> cover-letter template -> returned text updates field
  - logout clears protected stack
  - employer login (`demo-202605131244-01@notdjinni.test / User01Aa`) -> create vacancy -> details via `popUpTo` -> one Back returns `My Vacancies`
