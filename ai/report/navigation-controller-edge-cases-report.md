# Navigation Controller Edge Cases Report

## What was done

- Added local JVM unit coverage for `NavigationController` edge cases.
- Covered child back not invoking root callback.
- Covered `popUpTo` when anchor is already stack top.
- Covered inclusive root `popUpTo` keeping stack non-empty before adding the new key.
- Covered duplicate anchors using the last matching anchor.

## What files changed

- `app/src/test/kotlin/not/djinni/presentation/navigation/NavigationControllerTest.kt`
- `ai/report/navigation-controller-edge-cases-report.md`

## How to test

- Run `./gradlew :app:testDebugUnitTest --tests not.djinni.presentation.navigation.NavigationControllerTest`
