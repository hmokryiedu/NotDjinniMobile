# NotDjinni Project - Product Requirements Document

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

### 6.2 Preview Requirements
- All screens must have `@Preview` annotation
- Wrap preview content with `NotDjinniTheme { }`
- Create mock state for stateful screens
- Keep preview functions private

### 6.3 Screen Wrapper
- Use `Screen<T>` generic function for ViewModel injection
- Pattern: `Screen<ViewModelType> { viewModel -> /* UI */ }`

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

### 10.2 Adding New Route
1. Add screen to `Screens` sealed interface
2. Create route in appropriate route file
3. Register in navigation graph
4. Use `NavController` extensions for type-safe navigation

---

## Summary

This PRD establishes the foundation for consistent development in the NotDjinni project. When creating new features:

1. **Choose the right screen type** (stateless vs stateful)
2. **Follow the file structure** exactly as shown
3. **Use appropriate ViewModels** (`BaseViewModel` or `StateViewModel`)
4. **Create use cases** for business logic
5. **Follow naming conventions** consistently
6. **Include previews** for all screens
7. **Use Koin annotations** for dependency injection

Always refer to existing examples in the codebase when implementing new features.