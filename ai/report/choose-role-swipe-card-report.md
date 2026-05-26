# Choose Role Swipe Card Report

## What was done

- Replaced the old choose-role flow on `ChooseRoleScreen` with one swipe card interaction.
- Removed the two role rows, bottom `Continue` button, and extra status/pill UI.
- Updated the title to `What’s your role?` and added direction labels `← Employer` and `Seeker →`.
- Added idle card state with case icon and prompts `Your role?` and `Swipe to choose`.
- Added left swipe preview/commit for Employer with orange accent, glow, and content `Employer` / `Post jobs & hire`.
- Added right swipe preview/commit for Seeker with success accent, glow, and content `Seeker` / `Find & apply`.
- Added 30% drag preview threshold and 80% release commit threshold.
- Kept role persistence and existing post-selection navigation flow by replacing the old action pair with `ConfirmRole(role)` in the ViewModel.

## What files changed

- `app/src/main/kotlin/not/djinni/presentation/screens/auth/role/ChooseRoleScreen.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/auth/role/ChooseRoleAction.kt`
- `app/src/main/kotlin/not/djinni/presentation/screens/auth/role/ChooseRoleViewModel.kt`
- `ai/specs/choose-role-swipe-card-spec.md`

## How to test

1. Run `./gradlew :app:testDebugUnitTest --console=plain`.
2. Confirm result is `BUILD SUCCESSFUL`.
3. Run `./gradlew :app:installDebug --console=plain`.
4. Launch app on emulator `emulator-5554` Pixel_10 Android 16 and open choose-role screen.
5. Verify idle state:
   - title `What’s your role?`
   - labels `← Employer` and `Seeker →`
   - centered card with `Your role?` and `Swipe to choose`
   - no `Continue` button
6. Verify short drag below preview threshold keeps idle card content.
7. Verify left drag past preview threshold shows Employer preview with orange accent/glow.
8. Verify right drag past preview threshold shows Seeker preview with green accent/glow.
9. Verify release below commit threshold resets card to center and does not navigate.
10. Verify left release past commit threshold confirms Employer and continues through existing employer path.
11. Verify right release past commit threshold confirms Seeker and continues through existing seeker path.

Validation evidence:

- `./gradlew :app:testDebugUnitTest --console=plain` -> `BUILD SUCCESSFUL`
- `./gradlew :app:installDebug --console=plain` installed successfully
- Emulator QA passed on `emulator-5554` Pixel_10 Android 16:
  - idle
  - short drag
  - left/right preview
  - below-commit reset
  - left employer commit
  - right seeker commit
- Crash log buffer empty
