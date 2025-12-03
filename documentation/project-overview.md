# NotDjinni Project - Comprehensive Technical Overview

> **Last Updated:** December 2024  
> **Version:** 0.0.1  
> **Platform:** Android (Kotlin + Jetpack Compose)

This document provides a complete technical overview of the NotDjinni project — a job marketplace Android application connecting job seekers with employers. It covers architecture, patterns, best practices, and step-by-step guides for development.

---

## Table of Contents

1. [Project Overview](#1-project-overview)
2. [Technology Stack](#2-technology-stack)
3. [Architecture & Project Structure](#3-architecture--project-structure)
4. [Layer-by-Layer Guide](#4-layer-by-layer-guide)
5. [Screen Development Guide](#5-screen-development-guide)
6. [Network Layer Guide](#6-network-layer-guide)
7. [Repository Pattern Guide](#7-repository-pattern-guide)
8. [Use Case Pattern Guide](#8-use-case-pattern-guide)
9. [Navigation System](#9-navigation-system)
10. [Dependency Injection (Koin)](#10-dependency-injection-koin)
11. [UI Components Library](#11-ui-components-library)
12. [Theme System](#12-theme-system)
13. [Extensions & Utilities](#13-extensions--utilities)
14. [Code Style & Best Practices](#14-code-style--best-practices)
15. [File Naming Conventions](#15-file-naming-conventions)
16. [Checklists](#16-checklists)

---

## 1. Project Overview

**NotDjinni** is an Android job marketplace application that enables:
- **Job Seekers** to create profiles, browse vacancies, and apply for jobs
- **Employers** to create company profiles, post vacancies, and manage applications

The application follows **Clean Architecture** principles with clear separation of concerns across data, domain, and presentation layers.

### Key Features
- User authentication (Sign In / Sign Up)
- Role-based navigation (Seeker vs Employer)
- Profile management for both user types
- Vacancy creation and browsing
- Application tracking

---

## 2. Technology Stack

### Core
| Technology | Version | Purpose |
|------------|---------|---------|
| **Kotlin** | 2.2.21 | Programming language |
| **Jetpack Compose** | BOM 2025.11.01 | UI framework |
| **Android SDK** | 28-36 | Target platforms |
| **Java** | 21 | JVM target |

### Architecture & DI
| Technology | Version | Purpose |
|------------|---------|---------|
| **Koin** | 4.2.0-alpha3 | Dependency injection |
| **Koin Annotations** | 2.3.1 | Compile-time DI verification |
| **Navigation3** | 1.0.0 | Type-safe navigation |
| **Lifecycle** | 2.10.0 | ViewModel & lifecycle |

### Networking & Data
| Technology | Version | Purpose |
|------------|---------|---------|
| **Ktor** | 3.3.2 | HTTP client |
| **Room** | 2.8.4 | Local database |
| **DataStore** | 1.2.0 | Preferences storage |
| **Kotlinx Serialization** | 1.9.0 | JSON serialization |

### Utilities
| Technology | Version | Purpose |
|------------|---------|---------|
| **Timber** | 5.0.1 | Logging |
| **Kotlinx DateTime** | 0.7.1 | Date/time handling |
| **Kotlinx Collections Immutable** | 0.4.0 | Immutable collections |

---

## 3. Architecture & Project Structure

### 3.1 Clean Architecture Layers

```
┌─────────────────────────────────────────────────────────┐
│                    PRESENTATION                          │
│  (UI, ViewModels, State, Actions, SideEffects)          │
├─────────────────────────────────────────────────────────┤
│                      DOMAIN                              │
│  (Use Cases, Repository Interfaces, Validators)         │
├─────────────────────────────────────────────────────────┤
│                       DATA                               │
│  (Repository Implementations, Mappers, DataSources)     │
├─────────────────────────────────────────────────────────┤
│                     NETWORK                              │
│  (API Clients, Request/Response DTOs, Resources)        │
└─────────────────────────────────────────────────────────┘
```

### 3.2 Package Structure

```
not.djinni/
├── core/                          # Core utilities
│   ├── extension/                 # Kotlin extensions (Flow, Boolean, Number, List, etc.)
│   └── logging/                   # Timber-based logging utilities
│
├── data/                          # Data layer implementations
│   ├── mapper/                    # Response → Domain model mappers
│   ├── repository/                # Repository implementations (Default*)
│   └── validator/                 # Data validation implementations
│
├── datastore/                     # Local storage
│   ├── extension/                 # DataStore extensions
│   └── session/                   # Session management
│
├── di/                            # Dependency injection
│   ├── client/                    # HTTP client builders
│   ├── AppModule.kt               # Main Koin module
│   ├── NetworkModule.kt           # Network configuration
│   └── DatastoreModule.kt         # DataStore configuration
│
├── domain/                        # Domain layer (interfaces)
│   ├── repository/                # Repository interfaces
│   ├── usecase/                   # Use case implementations
│   │   ├── core/                  # Base UseCase interfaces
│   │   └── {feature}/             # Feature-specific use cases
│   └── validator/                 # Validator interfaces
│
├── model/                         # Domain models
│   ├── company/                   # Company-related models
│   ├── employer/                  # Employer-related models
│   ├── seeker/                    # Seeker-related models
│   │   └── vacancy/               # Vacancy models
│   └── role/                      # Role enums
│
├── network/                       # Network layer
│   ├── auth/                      # Authentication API
│   ├── common/                    # Shared network utilities
│   │   ├── extension/             # HttpResponse extensions
│   │   └── response/              # Common response types
│   ├── company/                   # Company API
│   ├── employer/                  # Employer API
│   ├── model/                     # Shared network models
│   ├── seeker/                    # Seeker API
│   ├── token/                     # Token management API
│   ├── user/                      # User API
│   └── vacancy/                   # Vacancy API
│
├── presentation/                  # UI layer
│   ├── core/                      # Core presentation components
│   │   ├── components/            # Reusable UI components
│   │   │   ├── base/              # Base components (Button, Text, TextField, etc.)
│   │   │   │   └── model/         # Component models (TextData, ButtonData, etc.)
│   │   │   └── snackbar/          # SnackBar components
│   │   └── extension/             # Presentation extensions
│   ├── navigation/                # Navigation setup
│   │   └── controller/            # Screen definitions & entries
│   ├── screens/                   # Screen implementations
│   │   ├── auth/                  # Authentication screens
│   │   ├── employer/              # Employer screens
│   │   ├── seeker/                # Seeker screens
│   │   └── splash/                # Splash screen
│   └── theme/                     # Theme configuration
│       └── icons/                 # Custom icon components
│
└── utils/                         # General utilities
    └── string/                    # String utilities
```

---

## 4. Layer-by-Layer Guide

### 4.1 Presentation Layer

The presentation layer handles UI and user interactions using **MVI (Model-View-Intent)** pattern.

#### Components per Screen:
| File | Purpose |
|------|---------|
| `{Feature}Screen.kt` | Composable UI entry point |
| `{Feature}ViewModel.kt` | Business logic & state management |
| `{Feature}State.kt` | Immutable UI state |
| `{Feature}Action.kt` | User actions/intents |
| `{Feature}SideEffect.kt` | One-time effects (navigation, toasts) |

### 4.2 Domain Layer

Pure Kotlin layer containing business logic interfaces.

#### Components:
| Type | Location | Purpose |
|------|----------|---------|
| **Use Cases** | `domain/usecase/` | Single business operations |
| **Repository Interfaces** | `domain/repository/` | Data operation contracts |
| **Validators** | `domain/validator/` | Data validation interfaces |

### 4.3 Data Layer

Implementation layer connecting domain with external sources.

#### Components:
| Type | Location | Purpose |
|------|----------|---------|
| **Repository Implementations** | `data/repository/` | Implement domain interfaces |
| **Mappers** | `data/mapper/` | DTO → Domain transformations |
| **Validator Implementations** | `data/validator/` | Validation logic |

### 4.4 Network Layer

HTTP communication layer using Ktor.

#### Components per Feature:
```
network/{feature}/
├── {Feature}DataSource.kt           # Interface
├── Default{Feature}DataSource.kt    # Implementation
├── resource/
│   └── {Feature}.kt                 # Ktor Resources (endpoints)
├── request/
│   └── {Operation}Request.kt        # Request DTOs
└── response/
    └── {Model}Response.kt           # Response DTOs
```

---

## 5. Screen Development Guide

### 5.1 Stateless Screen (No State Management)

**Use when:** Screen has no dynamic state, just displays static content.

#### Step 1: Create Screen File

```kotlin
// presentation/screens/example/ExampleScreen.kt
package not.djinni.presentation.screens.example

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import not.djinni.presentation.core.Screen
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
fun ExampleScreen() {
    Screen<ExampleViewModel> { viewModel ->
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

#### Step 2: Create ViewModel

```kotlin
// presentation/screens/example/ExampleViewModel.kt
package not.djinni.presentation.screens.example

import not.djinni.presentation.core.BaseViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ExampleViewModel : BaseViewModel()
```

### 5.2 Stateful Screen (With State Management)

**Use when:** Screen has dynamic state that changes based on user actions or data.

#### Step 1: Create State

```kotlin
// presentation/screens/example/ExampleState.kt
package not.djinni.presentation.screens.example

import androidx.compose.runtime.Immutable
import not.djinni.presentation.core.components.base.model.TextData

@Immutable
internal data class ExampleState(
    val isLoading: Boolean = false,
    val errorMessage: TextData? = null,
    val data: List<String> = emptyList(),
)
```

#### Step 2: Create Actions

```kotlin
// presentation/screens/example/ExampleAction.kt
package not.djinni.presentation.screens.example

internal sealed interface ExampleAction {
    data object LoadData : ExampleAction
    data class ItemClicked(val id: String) : ExampleAction
}
```

#### Step 3: Create Side Effects

```kotlin
// presentation/screens/example/ExampleSideEffect.kt
package not.djinni.presentation.screens.example

internal sealed interface ExampleSideEffect {
    data object NavigateBack : ExampleSideEffect
    data class NavigateToDetails(val id: String) : ExampleSideEffect
}
```

#### Step 4: Create ViewModel

```kotlin
// presentation/screens/example/ExampleViewModel.kt
package not.djinni.presentation.screens.example

import kotlinx.coroutines.flow.asSharedFlow
import not.djinni.core.extension.mutableSideEffect
import not.djinni.presentation.core.StateViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ExampleViewModel(
    private val exampleUseCase: ExampleUseCase,
) : StateViewModel<ExampleState>(ExampleState()) {

    private val _sideEffect = mutableSideEffect<ExampleSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun sendAction(action: ExampleAction) {
        when (action) {
            ExampleAction.LoadData -> loadData()
            is ExampleAction.ItemClicked -> onItemClicked(action.id)
        }
    }

    private fun loadData() {
        launch(loadingEnabled = true) {
            exampleUseCase()
                .onSuccess { data ->
                    updateState { copy(data = data, isLoading = false) }
                }
                .onFailure { error ->
                    updateState { copy(errorMessage = error.message.toTextData()) }
                }
        }
    }

    private fun onItemClicked(id: String) {
        launch {
            _sideEffect.emit(ExampleSideEffect.NavigateToDetails(id))
        }
    }
}
```

#### Step 5: Create Screen

```kotlin
// presentation/screens/example/ExampleScreen.kt
package not.djinni.presentation.screens.example

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import not.djinni.presentation.core.Screen
import not.djinni.presentation.core.extension.collectAsEffect
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
internal fun ExampleScreen(
    onNavigateToDetails: (String) -> Unit,
) {
    Screen<ExampleViewModel> { viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()

        Content(
            state = state,
            onAction = viewModel::sendAction
        )

        // Side effects MUST be at the bottom
        viewModel.sideEffect.collectAsEffect { effect ->
            when (effect) {
                is ExampleSideEffect.NavigateToDetails -> onNavigateToDetails(effect.id)
                ExampleSideEffect.NavigateBack -> { /* handle */ }
            }
        }
    }
}

@Composable
private fun Content(
    state: ExampleState,
    onAction: (ExampleAction) -> Unit,
) {
    // UI implementation
}

@Composable
@Preview
private fun Preview() {
    NotDjinniTheme {
        Content(
            state = ExampleState(),
            onAction = {}
        )
    }
}
```

---

## 6. Network Layer Guide

### 6.1 Create Data Source Interface

```kotlin
// network/example/ExampleDataSource.kt
package not.djinni.network.example

import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.example.response.ExampleResponse

interface ExampleDataSource {
    suspend fun getItems(): NetworkResponse<List<ExampleResponse>>
    suspend fun getItem(id: Long): NetworkResponse<ExampleResponse>
    suspend fun createItem(request: CreateExampleRequest): NetworkResponse<ExampleResponse>
}
```

### 6.2 Create Data Source Implementation

```kotlin
// network/example/DefaultExampleDataSource.kt
package not.djinni.network.example

import io.ktor.client.HttpClient
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.setBody
import not.djinni.network.common.extension.networkResponse
import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.example.resource.Example
import not.djinni.network.example.response.ExampleResponse
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single(binds = [ExampleDataSource::class])
internal class DefaultExampleDataSource(
    @Named("authenticated") private val httpClient: HttpClient
) : ExampleDataSource {

    override suspend fun getItems(): NetworkResponse<List<ExampleResponse>> {
        return httpClient
            .get(Example.List())
            .networkResponse()
    }

    override suspend fun getItem(id: Long): NetworkResponse<ExampleResponse> {
        return httpClient
            .get(Example.Details(id = id))
            .networkResponse()
    }

    override suspend fun createItem(
        request: CreateExampleRequest
    ): NetworkResponse<ExampleResponse> {
        return httpClient
            .post(Example()) { setBody(request) }
            .networkResponse()
    }
}
```

### 6.3 Create Ktor Resources

```kotlin
// network/example/resource/Example.kt
package not.djinni.network.example.resource

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Serializable
@Resource("/examples")
class Example {

    @Serializable
    @Resource("list")
    class List(
        val parent: Example = Example(),
        val limit: Int? = null,
        val offset: Int? = null,
    )

    @Serializable
    @Resource("{id}")
    class Details(
        val parent: Example = Example(),
        val id: Long,
    )
}
```

### 6.4 Create Request/Response DTOs

```kotlin
// network/example/request/CreateExampleRequest.kt
package not.djinni.network.example.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateExampleRequest(
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String,
)

// network/example/response/ExampleResponse.kt
package not.djinni.network.example.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExampleResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("description")
    val description: String,
)
```

### 6.5 NetworkResponse Pattern

```kotlin
// network/common/response/NetworkResponse.kt
sealed interface NetworkResponse<out T : Any> {
    data class Success<out T : Any>(val data: T) : NetworkResponse<T>
    data class Error(val error: String) : NetworkResponse<Nothing>
}

// network/common/extension/HttpResponse.kt
suspend inline fun <reified T : Any> HttpResponse.networkResponse(): NetworkResponse<T> {
    return when (status.value) {
        in 200..299 -> NetworkResponse.Success(body<T>())
        else -> {
            val message = runCatching { body<ErrorResponse>().message }
            NetworkResponse.Error(message.getOrDefault("Unknown error"))
        }
    }
}
```

---

## 7. Repository Pattern Guide

### 7.1 Create Repository Interface

```kotlin
// domain/repository/ExampleRepository.kt
package not.djinni.domain.repository

import not.djinni.model.Example

interface ExampleRepository {
    suspend fun getItems(): Result<List<Example>>
    suspend fun getItem(id: Long): Example?
    suspend fun createItem(name: String, description: String): Result<Example>
}
```

### 7.2 Create Repository Implementation

```kotlin
// data/repository/DefaultExampleRepository.kt
package not.djinni.data.repository

import not.djinni.data.mapper.toDomain
import not.djinni.domain.repository.ExampleRepository
import not.djinni.model.Example
import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.example.ExampleDataSource
import not.djinni.network.example.request.CreateExampleRequest
import org.koin.core.annotation.Single

@Single(binds = [ExampleRepository::class])
class DefaultExampleRepository(
    private val remoteDataSource: ExampleDataSource,
) : ExampleRepository {

    override suspend fun getItems(): Result<List<Example>> = runCatching {
        when (val response = remoteDataSource.getItems()) {
            is NetworkResponse.Success -> response.data.map { it.toDomain() }
            is NetworkResponse.Error -> throw Exception(response.error)
        }
    }

    override suspend fun getItem(id: Long): Example? {
        return when (val response = remoteDataSource.getItem(id)) {
            is NetworkResponse.Success -> response.data.toDomain()
            is NetworkResponse.Error -> null
        }
    }

    override suspend fun createItem(
        name: String,
        description: String
    ): Result<Example> = runCatching {
        val request = CreateExampleRequest(name = name, description = description)
        when (val response = remoteDataSource.createItem(request)) {
            is NetworkResponse.Success -> response.data.toDomain()
            is NetworkResponse.Error -> throw Exception(response.error)
        }
    }
}
```

### 7.3 Create Mapper

```kotlin
// data/mapper/ExampleMapper.kt
package not.djinni.data.mapper

import not.djinni.model.Example
import not.djinni.network.example.response.ExampleResponse

internal fun ExampleResponse.toDomain(): Example {
    return Example(
        id = id,
        name = name,
        description = description,
    )
}
```

---

## 8. Use Case Pattern Guide

### 8.1 Use Case Without Parameters

```kotlin
// domain/usecase/example/GetExamplesUseCase.kt
package not.djinni.domain.usecase.example

import not.djinni.domain.repository.ExampleRepository
import not.djinni.domain.usecase.core.UseCase
import not.djinni.model.Example
import org.koin.core.annotation.Factory

@Factory
class GetExamplesUseCase(
    private val exampleRepository: ExampleRepository,
) : UseCase<Result<List<Example>>> {

    override suspend fun invoke(): Result<List<Example>> {
        return exampleRepository.getItems()
    }
}
```

### 8.2 Use Case With Parameters

```kotlin
// domain/usecase/example/CreateExampleUseCase.kt
package not.djinni.domain.usecase.example

import not.djinni.domain.repository.ExampleRepository
import not.djinni.domain.usecase.core.UseCaseWithParams
import not.djinni.model.Example
import org.koin.core.annotation.Factory

@Factory
class CreateExampleUseCase(
    private val exampleRepository: ExampleRepository,
) : UseCaseWithParams<Result<Example>, CreateExampleUseCase.Params> {

    override suspend fun invoke(params: Params): Result<Example> = runCatching {
        require(params.name.isNotBlank()) { "Name cannot be empty" }
        exampleRepository.createItem(
            name = params.name,
            description = params.description
        ).getOrThrow()
    }

    data class Params(
        val name: String,
        val description: String,
    )
}
```

---

## 9. Navigation System

### 9.1 Define Screens

```kotlin
// presentation/navigation/controller/Screens.kt
@Stable
@Serializable
sealed interface Screens : NavKey {

    @Serializable
    data object Splash : Screens

    @Serializable
    data object Auth : Screens

    @Serializable
    sealed interface Seeker : Screens {
        @Serializable
        data object Main : Seeker

        @Serializable
        data class VacancyDetails(val vacancyId: Long) : Seeker
    }

    @Serializable
    sealed interface Employer : Screens {
        @Serializable
        data object Main : Employer

        @Serializable
        data object CreateVacancy : Employer
    }
}
```

### 9.2 Create Entry Definitions

```kotlin
// presentation/navigation/controller/ExampleEntry.kt
package not.djinni.presentation.navigation.controller

import androidx.navigation3.runtime.EntryProviderScope
import not.djinni.presentation.navigation.NavigationController
import not.djinni.presentation.screens.example.ExampleScreen

fun EntryProviderScope<Screens>.exampleEntry(
    controller: NavigationController,
) {
    entry<Screens.Example.List> {
        ExampleListScreen(
            onItemClick = { id -> controller.navigate(Screens.Example.Details(id)) }
        )
    }
    entry<Screens.Example.Details> { backstackEntry ->
        val id = backstackEntry.key.id
        ExampleDetailsScreen(
            id = id,
            onBack = { controller.popBackStack() }
        )
    }
}
```

### 9.3 Register Entry in NavDisplay

```kotlin
// presentation/navigation/NotDjinniNavDisplay.kt
@Composable
fun NotDjinniNavDisplay(
    modifier: Modifier = Modifier,
    controller: NavigationController,
) {
    NavDisplay(
        modifier = modifier,
        backStack = controller.stack,
        onBack = controller::popBackStack,
        entryProvider = entryProvider {
            authEntry(controller = controller)
            employerEntry(controller = controller)
            seekerEntry(controller = controller)
            splashEntry(controller = controller)
            exampleEntry(controller = controller)  // Add new entry
        },
        // ... rest of configuration
    )
}
```

---

## 10. Dependency Injection (Koin)

### 10.1 Annotations Reference

| Annotation | Usage | Lifecycle |
|------------|-------|-----------|
| `@KoinViewModel` | ViewModels | Scoped to navigation |
| `@Single` | Singletons | Application lifetime |
| `@Factory` | Use cases | New instance each call |
| `@Named("name")` | Qualifier | Distinguish implementations |

### 10.2 Binding Pattern

```kotlin
// Interface binding
@Single(binds = [ExampleRepository::class])
class DefaultExampleRepository(...) : ExampleRepository

// Multiple interface binding
@Single(binds = [Interface1::class, Interface2::class])
class MultiImplementation(...) : Interface1, Interface2
```

### 10.3 Named Injection

```kotlin
// Definition
@Single
@Named("authenticated")
fun authenticatedClient(): HttpClient = /* ... */

// Usage
class MyDataSource(
    @Named("authenticated") private val httpClient: HttpClient
)
```

### 10.4 Module Configuration

```kotlin
// di/AppModule.kt
@Module(includes = [NetworkModule::class, DatastoreModule::class])
@ComponentScan("not.djinni")
class AppModule
```

---

## 11. UI Components Library

### 11.1 Core Components

| Component | Purpose | Location |
|-----------|---------|----------|
| `Screen<VM>` | ViewModel injection wrapper with loading/snackbar | `presentation/core/Screen.kt` |
| `FullscreenColumn` | Standard full-screen scrollable layout | `components/base/FullscreenColumn.kt` |
| `NotDjinniText` | Text component with TextData support | `components/base/NotDjinniText.kt` |
| `NotDjinniTextField` | Styled text input | `components/base/NotDjinniTextField.kt` |
| `NotDjinniButton` | Primary button | `components/base/NotDjinniButton.kt` |
| `NotDjinniGradientButton` | Gradient styled button | `components/base/NotDjinniGradientButton.kt` |
| `NotDjinniLoader` | Loading indicator | `components/base/NotDjinniLoader.kt` |
| `VerticalSpacer` | Vertical spacing | `components/base/Spacer.kt` |
| `HorizontalSpacer` | Horizontal spacing | `components/base/Spacer.kt` |
| `MessageCard` | Error/info message display | `components/base/MessageCard.kt` |
| `VacancyCard` | Vacancy list item | `components/base/VacancyCard.kt` |
| `AlertContainer` | Alert/dialog wrapper | `components/base/AlertContainer.kt` |

### 11.2 Model Classes

| Model | Purpose |
|-------|---------|
| `TextData` | Wrapper for strings/resources |
| `ImageData` | Wrapper for images/resources |
| `ButtonData` | Button configuration |
| `SnackBarData` | SnackBar configuration |
| `VacancyCardData` | Vacancy card data |

### 11.3 TextData Usage

```kotlin
// From string resource
val text = R.string.hello_world.toTextData()

// From string
val text = "Hello World".toTextData()

// From annotated string
val text = buildAnnotatedString { ... }.toTextData()

// Empty
val text = TextData.Empty
```

---

## 12. Theme System

### 12.1 Theme Components

```kotlin
// Usage in composables
NotDjinniTheme.colors.onSurface      // Colors
NotDjinniTheme.typography.title1     // Typography
NotDjinniTheme.offsets.medium        // Spacing
NotDjinniTheme.shapes.medium         // Shapes
```

### 12.2 Available Colors

```kotlin
object NotDjinniColor {
    val background: Color
    val surface: Color
    val onSurface: Color
    val primary: Color
    val error: Color
    val forcedBlack: Color
    val forcedWhite: Color
    // ... more colors
}
```

### 12.3 Available Offsets

```kotlin
object NotDjinniOffset {
    val empty: Dp      // 0.dp
    val tiny: Dp       // 4.dp
    val small: Dp      // 8.dp
    val mid: Dp        // 12.dp
    val medium: Dp     // 16.dp
    val average: Dp    // 20.dp
    val large: Dp      // 24.dp
    val huge: Dp       // 32.dp
    val giant: Dp      // 48.dp
}
```

---

## 13. Extensions & Utilities

### 13.1 Core Extensions (`core/extension/`)

```kotlin
// Flow.kt - Side effect creation
fun <T> mutableSideEffect() = MutableSharedFlow<T>(
    extraBufferCapacity = 2,
    onBufferOverflow = BufferOverflow.DROP_OLDEST
)

// Boolean.kt
val Boolean.not: Boolean get() = !this

// Number.kt
fun Int.orZero(): Int = this ?: 0
```

### 13.2 Presentation Extensions (`presentation/core/extension/`)

```kotlin
// TextData.kt
fun String?.toTextData(): TextData
fun @receiver:StringRes Int.toTextData(): TextData

// Flow.kt - Side effect collection
@Composable
fun <T> Flow<T>.collectAsEffect(action: suspend (T) -> Unit)

// Modifier.kt
fun Modifier.clickableNoRipple(onClick: () -> Unit): Modifier
fun Modifier.applyIf(condition: Boolean, block: Modifier.() -> Modifier): Modifier
```

---

## 14. Code Style & Best Practices

### 14.1 Critical Rules

1. **No unnecessary comments** - Code should be self-explanatory
2. **Side effects at bottom** - Always place `collectAsEffect` after Content
3. **Constants at file bottom** - In composable files, constants go at the end
4. **No magic numbers** - Extract to named constants
5. **Compact composables** - Minimal blank lines between components

### 14.2 State Management

```kotlin
// Correct state update
updateState { copy(isLoading = true) }

// Alternative
mutableState.update { it.copy(isLoading = true) }
```

### 14.3 Error Handling

```kotlin
// In ViewModel
launch {
    useCase()
        .onSuccess { data -> updateState { copy(data = data) } }
        .onFailure { error -> 
            showSnackBar(SnackBarData(error.message.toTextData()))
        }
}
```

### 14.4 Composable Structure Order

```kotlin
@Composable
fun ExampleScreen() {
    Screen<ExampleViewModel> { viewModel ->
        // 1. State collection
        val state by viewModel.state.collectAsStateWithLifecycle()
        
        // 2. Content
        Content(state = state, onAction = viewModel::sendAction)
        
        // 3. Side effects (MUST be at bottom)
        viewModel.sideEffect.collectAsEffect { effect ->
            // Handle effects
        }
    }
}
```

### 14.5 Alert/Dialog Pattern

```kotlin
@Composable
fun SelectionAlert(
    items: List<Item>,
    selected: Item?,
    onDismiss: () -> Unit,
    onApply: (Item) -> Unit
) {
    // Use ID-based selection, not index
    var selectedId by remember { mutableStateOf(selected?.id) }

    Dialog(onDismissRequest = onDismiss) {
        LazyColumn {
            items(items = items, key = { it.id }) { item ->
                // Use clickableNoRipple
                Row(
                    modifier = Modifier.clickableNoRipple { 
                        selectedId = item.id 
                    }
                ) {
                    // Item content
                }
            }
        }
    }
}
```

---

## 15. File Naming Conventions

### 15.1 Presentation Layer

| Type | Pattern | Example |
|------|---------|---------|
| Screen | `{Feature}Screen.kt` | `AuthScreen.kt` |
| ViewModel | `{Feature}ViewModel.kt` | `AuthViewModel.kt` |
| State | `{Feature}State.kt` | `AuthState.kt` |
| Action | `{Feature}Action.kt` | `AuthAction.kt` |
| SideEffect | `{Feature}SideEffect.kt` | `AuthSideEffect.kt` |
| Alert Enum | `{Feature}Alert.kt` | `CreateProfileAlert.kt` |
| Entry | `{Feature}Entry.kt` | `AuthEntry.kt` |

### 15.2 Network Layer

| Type | Pattern | Example |
|------|---------|---------|
| DataSource Interface | `{Feature}DataSource.kt` | `EmployerDataSource.kt` |
| DataSource Impl | `Default{Feature}DataSource.kt` | `DefaultEmployerDataSource.kt` |
| Resource | `{Feature}.kt` | `Employer.kt` |
| Request DTO | `{Operation}Request.kt` | `CreateProfileRequest.kt` |
| Response DTO | `{Model}Response.kt` | `ProfileResponse.kt` |

### 15.3 Domain/Data Layer

| Type | Pattern | Example |
|------|---------|---------|
| Repository Interface | `{Feature}Repository.kt` | `EmployerRepository.kt` |
| Repository Impl | `Default{Feature}Repository.kt` | `DefaultEmployerRepository.kt` |
| Use Case | `{Action}{Feature}UseCase.kt` | `GetEmployerProfileUseCase.kt` |
| Mapper | `{Model}Mapper.kt` | `EmployerMapper.kt` |
| Validator Interface | `{Feature}Validator.kt` | `AuthDataValidator.kt` |
| Validator Impl | `Default{Feature}Validator.kt` | `DefaultAuthDataValidator.kt` |

---

## 16. Checklists

### 16.1 New Stateful Screen Checklist

- [ ] Create package: `presentation/screens/{feature}/`
- [ ] Create `{Feature}State.kt` with `@Immutable` data class
- [ ] Create `{Feature}Action.kt` sealed interface
- [ ] Create `{Feature}SideEffect.kt` sealed interface
- [ ] Create `{Feature}ViewModel.kt` extending `StateViewModel<State>`
- [ ] Add `@KoinViewModel` annotation to ViewModel
- [ ] Create `{Feature}Screen.kt` with Screen composable
- [ ] Use `collectAsStateWithLifecycle()` for state
- [ ] Place `collectAsEffect` at bottom of Screen
- [ ] Create private `Content` composable
- [ ] Add `@Preview` with mock state
- [ ] Create navigation entry in `{Feature}Entry.kt`
- [ ] Register entry in `NotDjinniNavDisplay.kt`

### 16.2 New Network Feature Checklist

- [ ] Create package: `network/{feature}/`
- [ ] Create `{Feature}DataSource.kt` interface
- [ ] Create `Default{Feature}DataSource.kt` with `@Single(binds = [...])`
- [ ] Create `resource/{Feature}.kt` with Ktor Resources
- [ ] Create `request/` folder with request DTOs
- [ ] Create `response/` folder with response DTOs
- [ ] Use `@Serializable` and `@SerialName` annotations

### 16.3 New Repository Checklist

- [ ] Create `domain/repository/{Feature}Repository.kt` interface
- [ ] Create `data/repository/Default{Feature}Repository.kt`
- [ ] Add `@Single(binds = [{Feature}Repository::class])`
- [ ] Create mapper in `data/mapper/{Feature}Mapper.kt`
- [ ] Use `runCatching` for error handling

### 16.4 New Use Case Checklist

- [ ] Create package: `domain/usecase/{feature}/`
- [ ] Extend `UseCase<T>` or `UseCaseWithParams<T, P>`
- [ ] Add `@Factory` annotation
- [ ] Create `Params` data class if needed
- [ ] Inject repository via constructor
- [ ] Use `runCatching` for error handling

---

## Summary

This document establishes the foundation for consistent development in the NotDjinni project. Key principles:

1. **Follow Clean Architecture** - Separate concerns across layers
2. **Use MVI Pattern** - State, Actions, SideEffects for UI
3. **Type-safe Navigation** - Serializable screen definitions
4. **Koin Annotations** - Compile-time DI verification
5. **Consistent Naming** - Follow file naming conventions
6. **No Comments** - Self-documenting code
7. **Constants at Bottom** - In composable files

For detailed implementation examples, refer to existing code in the repository.
