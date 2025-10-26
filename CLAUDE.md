# NotDjinni Project - Product Requirements Document

This document consolidates the coding standards, project structure, and implementation patterns for the NotDjinni project. It is the single source of truth for project development guidelines.

### ⚠️ Important Maintenance Rules

**1. Documentation Synchronization**
Any changes to project rules, structure, or implementation patterns must be documented in this file immediately. If you modify:
- Project structure or package organization
- Architecture patterns or ViewModel approaches
- Naming conventions
- Development workflows or best practices
- Navigation patterns
- Any other structural or organizational changes

You **must** update this document to reflect those changes. Keeping this document in sync with the actual codebase is critical for team consistency and onboarding.

**2. Code Comments Policy**
Do not add comments to code unless explicitly requested. Comments should be added only when:
- The user specifically asks for them
- The code complexity genuinely requires explanation
- The code uses non-obvious algorithms or patterns

This keeps the codebase clean and maintainable. Self-explanatory code with clear naming conventions is preferred.

## Project Overview

NotDjinni is an Android application built with Kotlin, Jetpack Compose, and Clean Architecture principles. This document defines the coding standards, project structure, and implementation patterns for the project.

---

## 1. Project Structure

### 1.1 Package Organization
```
not.djinni/
├── core/                          # Core utilities and extensions
│   ├── extension/                 # Kotlin extension functions
│   └── logging/                   # Logging utilities
├── domain/                        # Business logic layer
│   └── usecase/                   # Use case implementations
│       ├── core/                  # Base use case interfaces
│       └── {feature}/             # Feature-specific use cases
├── presentation/                  # UI layer
│   ├── core/                      # Core presentation components
│   │   ├── components/            # Reusable UI components
│   │   └── extension/             # Presentation-related extensions
│   ├── navigation/                # Navigation logic
│   ├── screens/                   # Screen implementations
│   │   └── {screen_name}/         # Each screen in its own package
│   └── theme/                     # Theme configuration
└── di/                            # Dependency injection
```

---

## 2. Creating New Screens

### 2.1 Stateless Screen (Simple Screen)

**Location:** `app/src/main/kotlin/not/djinni/presentation/screens/{screen_name}/`

**Required Files:**
1. `{ScreenName}Screen.kt` - Composable UI
2. `{ScreenName}ViewModel.kt` - ViewModel

**Example Structure:**

#### Screen File (`SplashScreen.kt`)
```kotlin
package not.djinni.presentation.screens.splash

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import not.djinni.presentation.core.Screen
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
fun SplashScreen() {
    Screen<SplashViewModel> { viewModel ->
        Content()
    }
}

@Composable
private fun Content() {
    // UI implementation
}

@Composable
@Preview
private fun Preview() {
    NotDjinniTheme { Content() }
}
```

#### ViewModel File (`SplashViewModel.kt`)
```kotlin
package not.djinni.presentation.screens.splash

import not.djinni.presentation.core.BaseViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SplashViewModel : BaseViewModel()
```

### 2.2 Stateful Screen (Screen with State Management)

**Location:** `app/src/main/kotlin/not/djinni/presentation/screens/{screen_name}/`

**Required Files:**
1. `{ScreenName}Screen.kt` - Composable UI
2. `{ScreenName}ViewModel.kt` - StateViewModel
3. `{ScreenName}State.kt` - State data class

**Example Structure:**

#### State File (`ExampleState.kt`)
```kotlin
package not.djinni.presentation.screens.example

import androidx.compose.runtime.Immutable

@Immutable
data class ExampleState(
    val mockData: String = "",
)
```

#### ViewModel File (`ExampleStateViewModel.kt`)
```kotlin
package not.djinni.presentation.screens.example

import not.djinni.presentation.core.StateViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ExampleStateViewModel : StateViewModel<ExampleState>(ExampleState())
```

#### Screen File (`ExampleWithStateScreen.kt`)
```kotlin
package not.djinni.presentation.screens.example

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import not.djinni.presentation.core.Screen
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
fun ExampleWithStateScreen() {
    Screen<ExampleStateViewModel> { viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()

        Content(state = state)
    }
}

@Composable
private fun Content(state: ExampleState) {
    // UI implementation with state
}

@Composable
@Preview
private fun Preview() {
    NotDjinniTheme {
        val state = ExampleState()
        Content(state)
    }
}
```

---

## 3. Creating Use Cases

### 3.1 Location
`app/src/main/kotlin/not/djinni/domain/usecase/{feature}/`

### 3.2 Use Case Without Parameters

**Base Interface:** `UseCase<T>`

**Example:**
```kotlin
package not.djinni.domain.usecase.example

import not.djinni.domain.usecase.core.UseCase
import org.koin.core.annotation.Factory

/**
 * Return type can be any type provided as generic for UseCase
 */
@Factory
class ExampleUseCase : UseCase<String> {

    override suspend fun invoke(): String {
        return ""
    }
}
```

### 3.3 Use Case With Parameters

**Base Interface:** `UseCaseWithParams<T, P>`

**Example:**
```kotlin
package not.djinni.domain.usecase.example

import not.djinni.domain.usecase.core.UseCaseWithParams
import org.koin.core.annotation.Factory

/**
 * Return type can be any type provided as generic for UseCaseWithParams
 * Params can be extracted to data class or can be any other type
 */
@Factory
class ExampleWithParamsUseCase : UseCaseWithParams<String, ExampleWithParamsUseCase.Params> {

    override suspend fun invoke(params: Params): String {
        return params.data
    }

    data class Params(
        val data: String
    )
}
```

---

## 4. Naming Conventions

### 4.1 Files and Classes
- **Screens:** `{ScreenName}Screen.kt`, `{ScreenName}ViewModel.kt`, `{ScreenName}State.kt`
- **Use Cases:** `{Feature}{Action}UseCase.kt` (e.g., `GetUserProfileUseCase.kt`)
- **Extensions:** `{Type}.kt` (e.g., `String.kt`, `Flow.kt`)

### 4.2 Packages
- **Screen packages:** Use lowercase with descriptive names (e.g., `splash`, `profile`, `settings`)
- **Use case packages:** Group by feature/domain (e.g., `auth`, `user`, `payment`)

### 4.3 Functions
- **Composables:** PascalCase, starting with component name (e.g., `SplashScreen()`, `Content()`)
- **Private Composables:** Use `private` modifier (e.g., `private fun Content()`)
- **Use Cases:** Implement `suspend operator fun invoke()`

---

## 5. Architecture Patterns

### 5.1 ViewModel Types

#### BaseViewModel
- For screens without state management
- Provides loading state and snackbar functionality
- Use `launch()` method for coroutine execution
- Located: `presentation/core/BaseViewModel.kt`

#### StateViewModel<S>
- Extends `BaseViewModel`
- For screens requiring state management
- Exposes `state` as `StateFlow<S>`
- Update state using `mutableState.update { }` or direct assignment
- Located: `presentation/core/BaseViewModel.kt`

### 5.2 State Management
- All state classes must be annotated with `@Immutable`
- Use data classes for state
- Provide default values for all properties
- Collect state using `collectAsStateWithLifecycle()` in composables

### 5.3 Dependency Injection
- Use Koin for dependency injection
- ViewModels: `@KoinViewModel` annotation
- Use Cases: `@Factory` annotation
- Repositories/Services: `@Single` or `@Factory` based on lifecycle needs

---

## 6. UI Components Standards

### 6.1 Composable Structure
1. **Main Screen Composable:** Entry point, handles ViewModel
2. **Content Composable:** Private, contains UI logic
3. **Preview Composable:** For Android Studio preview, uses theme wrapper

### 6.1.1 Side Effects and LaunchedEffects Placement
**CRITICAL RULE - MUST BE FOLLOWED STRICTLY:**

The order of elements in a composable function MUST be:
1. State collection (e.g., `val state by viewModel.state.collectAsStateWithLifecycle()`)
2. Content composable call
3. **Side effect collections and LaunchedEffects at the BOTTOM** (after Content)

**Rules:**
- **Side effect collection** must use `collectAsEffect { effect -> }` without LaunchedEffect wrapper
- **All side effect collections and LaunchedEffects** MUST be placed at the absolute bottom of the composable function
- They must come AFTER the Content composable call, not before
- This ensures consistent code organization and readability

**Example:**
```kotlin
@Composable
fun ExampleScreen(onNavigate: () -> Unit) {
    Screen<ExampleViewModel> { viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()

        Content(state = state)

        viewModel.sideEffect.collectAsEffect { effect ->
            when (effect) {
                ExampleSideEffect.Navigate -> onNavigate()
            }
        }
    }
}
```

### 6.2 Preview Requirements
- All screens must have `@Preview` annotation
- Wrap preview content with `NotDjinniTheme { }`
- Create mock state for stateful screens
- Keep preview functions private

### 6.3 Screen Wrapper
- Use `Screen<T>` generic function for ViewModel injection
- Pattern: `Screen<ViewModelType> { viewModel -> /* UI */ }`

### 6.4 Code Formatting and Constants

#### Spacing Rules
- Do NOT add unnecessary blank lines between UI component calls in Composable functions
- Keep composable code compact and readable without extra vertical spacing
- Only add blank lines between logical sections when absolutely necessary

**Example:**
```kotlin
@Composable
fun Content() {
    Column {
        NotDjinniText(data = title)
        VerticalSpacer(offset)
        NotDjinniTextField(state = state)
        VerticalSpacer(offset)
        NotDjinniButton(onClick = {})
    }
}
```

#### Magic Numbers and Constants
- **Extract all magic numbers to constants** to avoid hard-coded values in composable code
- Constants should have descriptive names in UPPER_SNAKE_CASE
- In **Composable files**, constants MUST be placed at the **bottom of the file**, after all composable functions
- Use `private const val` for file-level constants

**Example:**
```kotlin
@Composable
fun PersonalInfoStep() {
    val state = rememberTextFieldState()
    NotDjinniTextField(
        state = state,
        lineLimits = TextFieldLineLimits.MultiLine(
            minHeightInLines = ABOUT_ME_MIN_LINES,
            maxHeightInLines = ABOUT_ME_MAX_LINES
        ),
    )
}

@Composable
@Preview
private fun Preview() {
    NotDjinniTheme { PersonalInfoStep() }
}

private const val ABOUT_ME_MIN_LINES = 4
private const val ABOUT_ME_MAX_LINES = 6
```

---

## 7. Best Practices

### 7.1 Code Organization
- Keep files focused on single responsibility
- Group related functionality in feature packages
- Use private visibility for internal composables
- Extract reusable components to `presentation/core/components/`

### 7.2 State Updates
```kotlin
// In ViewModel
mutableState.update { currentState ->
    currentState.copy(property = newValue)
}
```

### 7.3 Use Case Invocation
```kotlin
// In ViewModel
launch {
    val result = useCase() // or useCase(params)
    // Handle result
}
```

### 7.4 Loading States
```kotlin
// In ViewModel
launch(loadingEnabled = true) {
    // Long running operation
}
```

### 7.5 Error Handling
- Errors are automatically caught by `BaseViewModel.launch()`
- Use `showSnackBar()` to display error messages to users
- Logging is handled automatically

---

## 8. File Creation Checklist

### New Stateless Screen
- [ ] Create package: `presentation/screens/{screen_name}/`
- [ ] Create `{ScreenName}Screen.kt` with Screen composable
- [ ] Create `{ScreenName}ViewModel.kt` extending `BaseViewModel`
- [ ] Add `@KoinViewModel` annotation to ViewModel
- [ ] Implement Content composable
- [ ] Add Preview composable with theme wrapper
- [ ] Register navigation route (if needed)

### New Stateful Screen
- [ ] Create package: `presentation/screens/{screen_name}/`
- [ ] Create `{ScreenName}State.kt` with `@Immutable` data class
- [ ] Create `{ScreenName}ViewModel.kt` extending `StateViewModel<State>`
- [ ] Create `{ScreenName}Screen.kt` with Screen composable
- [ ] Add `@KoinViewModel` annotation to ViewModel
- [ ] Collect state using `collectAsStateWithLifecycle()`
- [ ] Implement Content composable with state parameter
- [ ] Add Preview composable with mock state
- [ ] Register navigation route (if needed)

### New Use Case
- [ ] Create package: `domain/usecase/{feature}/`
- [ ] Extend `UseCase<T>` or `UseCaseWithParams<T, P>`
- [ ] Add `@Factory` annotation
- [ ] Implement `invoke()` method
- [ ] Add KDoc comments explaining purpose
- [ ] Create Params data class (if using parameters)
- [ ] Inject dependencies via constructor

---

## 9. Common Extensions

### 9.1 Extension Locations
- **Core extensions:** `core/extension/` (e.g., List, Flow, Date, Number, Boolean)
- **Presentation extensions:** `presentation/core/extension/` (e.g., TextData, ImageData, Modifier)

### 9.2 Creating Extensions
- Group extensions by type in dedicated files
- Use descriptive function names
- Document complex extensions with KDoc
- Consider nullability and edge cases

---

## 10. Navigation

### 10.1 Navigation Setup
- Screens are defined in `presentation/navigation/controller/Screens.kt`
- Routes are created using `ConfigRouteCreator`
- Navigation handled by `NotDjinniNavController`

### 10.2 Navigation Destination Creation

Each screen requires a dedicated route file that defines the navigation destination and route builder function.

**Location:** `app/src/main/kotlin/not/djinni/presentation/navigation/controller/{Feature}Route.kt`

**Steps to create a new navigation destination:**

1. **Create Route File** (e.g., `AuthRoute.kt`):
```kotlin
package not.djinni.presentation.navigation.controller

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import not.djinni.presentation.screens.auth.AuthScreen

@Serializable
data object Auth : Screens

fun NavGraphBuilder.authRoute() {
    composable<Auth> { AuthScreen() }
}
```

2. **Register Route in Navigation Graph** (`NotDjinniNavController.kt`):
   - Import the route builder function: `import not.djinni.presentation.navigation.controller.authRoute`
   - Add route to NavHost builder: `authRoute()`

3. **Pattern Details:**
   - Use `@Serializable` annotation for type-safe navigation
   - Create `data object` that extends `Screens` sealed interface
   - Name the route builder function with `Route` suffix in lowercase: `authRoute()`, `authRoute()`
   - Each route should be in its own file named `{FeatureName}Route.kt`

### 10.3 Adding New Route
1. Add screen to `Screens` sealed interface (via `@Serializable data object` in route file)
2. Create route file with screen definition and route builder function
3. Import and register route builder in `NotDjinniNavController.kt`
4. Use `NavController` extensions for type-safe navigation

---

## Summary

This document establishes the foundation for consistent development in the NotDjinni project. When creating new features:

1. **Choose the right screen type** (stateless vs stateful)
2. **Follow the file structure** exactly as shown
3. **Use appropriate ViewModels** (`BaseViewModel` or `StateViewModel`)
4. **Create use cases** for business logic
5. **Follow naming conventions** consistently
6. **Include previews** for all screens
7. **Use Koin annotations** for dependency injection

Always refer to existing examples in the codebase when implementing new features. If significant architectural changes are made, update this document to reflect the new patterns and standards.