# NotDjinni Project - Technical Overview

This document provides a technical overview of the NotDjinni project architecture, patterns, and code conventions based on codebase analysis.

---

## 1. Project Architecture

### 1.1 Layer Structure

```
not.djinni/
├── core/                      # Core utilities
│   ├── extension/             # Kotlin extensions (Flow, Boolean, Number, etc.)
│   └── logging/               # Logging utilities
├── data/                      # Data layer implementations
│   ├── mapper/                # Response → Domain mappers
│   ├── repository/            # Repository implementations (Default*)
│   └── validator/             # Data validation implementations
├── datastore/                 # Local storage
├── di/                        # Dependency injection
├── domain/                    # Domain layer (interfaces)
│   ├── repository/            # Repository interfaces
│   ├── usecase/               # Use case implementations
│   └── validator/             # Validator interfaces
├── model/                     # Domain models
│   ├── company/               # Company-related models
│   ├── employer/              # Employer-related models
│   ├── seeker/                # Seeker-related models
│   └── role/                  # Role models
├── network/                   # Network layer
│   ├── auth/                  # Authentication network
│   ├── common/                # Shared network utilities
│   ├── employer/              # Employer API
│   ├── model/                 # Shared network models
│   └── seeker/                # Seeker API
└── presentation/              # UI layer
    ├── core/                  # Core presentation components
    │   ├── components/        # Reusable UI components
    │   └── extension/         # Presentation extensions
    ├── navigation/            # Navigation setup
    ├── screens/               # Screen implementations
    └── theme/                 # Theme configuration
```

---

## 2. Network Layer Pattern

### 2.1 Data Source Structure

Each feature module follows this pattern:

```
network/{feature}/
├── {Feature}DataSource.kt           # Interface
├── Default{Feature}DataSource.kt    # Implementation with @Single annotation
├── resource/
│   └── {Feature}.kt                 # Ktor Resources definitions
├── request/
│   └── {Operation}Request.kt        # Request DTOs
└── response/
    └── {Model}Response.kt           # Response DTOs
```

### 2.2 Data Source Interface Example

```kotlin
package not.djinni.network.employer

import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.employer.response.EmployerProfileResponse

interface EmployerDataSource {
    suspend fun getProfile(): NetworkResponse<EmployerProfileResponse>
}
```

### 2.3 Data Source Implementation Example

```kotlin
package not.djinni.network.employer

import io.ktor.client.HttpClient
import io.ktor.client.plugins.resources.get
import not.djinni.network.common.extension.networkResponse
import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.employer.resource.Employer
import not.djinni.network.employer.response.EmployerProfileResponse
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single(binds = [EmployerDataSource::class])
internal class DefaultEmployerDataSource(
    @Named("authenticated") private val httpClient: HttpClient
) : EmployerDataSource {

    override suspend fun getProfile(): NetworkResponse<EmployerProfileResponse> {
        return httpClient
            .get(Employer.Profile())
            .networkResponse<EmployerProfileResponse>()
    }
}
```

### 2.4 Ktor Resources Definition

```kotlin
package not.djinni.network.employer.resource

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Serializable
@Resource("/employer")
class Employer {
    @Serializable
    @Resource("profile")
    class Profile(val parent: Employer = Employer())
}
```

### 2.5 Network Response Pattern

```kotlin
package not.djinni.network.common.response

sealed interface NetworkResponse<out T : Any> {
    data class Success<out T : Any>(val data: T) : NetworkResponse<T>
    data class Error(val error: String) : NetworkResponse<Nothing>
}
```

### 2.6 HttpResponse Extension

```kotlin
suspend inline fun <reified T : Any> HttpResponse.networkResponse(): NetworkResponse<T> {
    return when (status.value) {
        in 200..299 -> NetworkResponse.Success(body<T>())
        else -> NetworkResponse.Error(body<ErrorResponse>().message)
    }
}
```

---

## 3. Repository Layer Pattern

### 3.1 Repository Interface (Domain)

Location: `domain/repository/`

```kotlin
package not.djinni.domain.repository

import not.djinni.model.employer.EmployerProfile

interface EmployerRepository {
    suspend fun getProfile(): EmployerProfile?
}
```

### 3.2 Repository Implementation (Data)

Location: `data/repository/`

```kotlin
package not.djinni.data.repository

import not.djinni.data.mapper.toDomain
import not.djinni.domain.repository.EmployerRepository
import not.djinni.model.employer.EmployerProfile
import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.employer.EmployerDataSource
import org.koin.core.annotation.Single

@Single(binds = [EmployerRepository::class])
class DefaultEmployerRepository(
    private val remoteDataSource: EmployerDataSource,
) : EmployerRepository {

    override suspend fun getProfile(): EmployerProfile? {
        return when (val response = remoteDataSource.getProfile()) {
            is NetworkResponse.Success -> response.data.toDomain()
            is NetworkResponse.Error -> null
        }
    }
}
```

---

## 4. Mapper Pattern

Location: `data/mapper/`

```kotlin
package not.djinni.data.mapper

import not.djinni.model.employer.EmployerProfile
import not.djinni.network.employer.response.EmployerProfileResponse

internal fun EmployerProfileResponse.toDomain(): EmployerProfile {
    return EmployerProfile(
        id = id,
        role = role,
        company = company.toDomain()
    )
}
```

```kotlin
package not.djinni.data.mapper

import not.djinni.model.company.Company
import not.djinni.network.employer.response.CompanyResponse

fun CompanyResponse.toDomain(): Company {
    return Company(
        id = id,
        name = name,
        description = description,
        website = website,
    )
}
```

---

## 5. Use Case Pattern

Location: `domain/usecase/`

### 5.1 Base Interfaces

```kotlin
// UseCase without parameters
interface UseCase<T> {
    suspend operator fun invoke(): T
}

// UseCase with parameters
interface UseCaseWithParams<T, P> {
    suspend operator fun invoke(params: P): T
}
```

### 5.2 Use Case Implementation

```kotlin
package not.djinni.domain.usecase.example

import not.djinni.domain.usecase.core.UseCase
import org.koin.core.annotation.Factory

@Factory
class ExampleUseCase : UseCase<String> {
    override suspend fun invoke(): String {
        return ""
    }
}
```

---

## 6. Presentation Layer Pattern

### 6.1 Screen Structure

Each screen follows this pattern:

```
presentation/screens/{feature}/
├── {Feature}Screen.kt
├── {Feature}ViewModel.kt
├── {Feature}State.kt
├── {Feature}Action.kt
├── {Feature}SideEffect.kt
├── alert/                    # Optional - alert definitions
└── model/                    # Optional - screen-specific models
```

### 6.2 ViewModel Pattern

```kotlin
package not.djinni.presentation.screens.seeker.profile.create

import kotlinx.coroutines.flow.asSharedFlow
import not.djinni.core.extension.mutableSideEffect
import not.djinni.domain.repository.SeekerRepository
import not.djinni.presentation.core.StateViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class CreateSeekerProfileViewModel(
    private val seekerRepository: SeekerRepository,
) : StateViewModel<CreateSeekerProfileState>(CreateSeekerProfileState()) {

    private val _sideEffect = mutableSideEffect<CreateSeekerProfileSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun sendAction(action: CreateSeekerProfileAction) {
        when (action) {
            is CreateSeekerProfileAction.CreateProfile -> createProfile(action)
            // ...
        }
    }

    private fun createProfile(data: CreateSeekerProfileAction.CreateProfile) {
        launch {
            // Implementation
            _sideEffect.emit(CreateSeekerProfileSideEffect.NavigateToHome)
        }
    }
}
```

### 6.3 State Pattern

```kotlin
package not.djinni.presentation.screens.seeker.profile.create

import androidx.compose.runtime.Immutable
import not.djinni.presentation.core.components.base.model.TextData

@Immutable
internal data class CreateSeekerProfileState(
    val message: TextData? = null,
    val currentAlert: CreateProfileAlert? = null,
    val workExperiences: List<WorkExperienceData> = emptyList(),
)
```

### 6.4 Action Pattern

```kotlin
package not.djinni.presentation.screens.seeker.profile.create

internal sealed interface CreateSeekerProfileAction {
    data object ShowAddWorkExperienceAlert : CreateSeekerProfileAction
    data class CreateProfile(
        val speciality: String,
        val desiredSalary: String,
    ) : CreateSeekerProfileAction
}
```

### 6.5 SideEffect Pattern

```kotlin
package not.djinni.presentation.screens.seeker.profile.create

internal sealed interface CreateSeekerProfileSideEffect {
    data object NavigateToHome : CreateSeekerProfileSideEffect
}
```

### 6.6 Screen Composable Pattern

```kotlin
@Composable
internal fun CreateSeekerProfileScreen(onHome: () -> Unit) {
    Screen<CreateSeekerProfileViewModel> { viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()

        // Main content with AnimatedContent for overlays
        AnimatedContent(
            targetState = state.currentAlert == CreateProfileAlert.ADD_WORK_EXPERIENCE,
            transitionSpec = { fadeIn() togetherWith fadeOut() }
        ) { isAlertVisible ->
            if (isAlertVisible) {
                AddWorkExperienceContent(onAction = viewModel::sendAction)
            } else {
                Content(state = state, onAction = viewModel::sendAction)
            }
        }

        // Side effects at the bottom
        viewModel.sideEffect.collectAsEffect { effect ->
            when (effect) {
                CreateSeekerProfileSideEffect.NavigateToHome -> onHome()
            }
        }
    }
}
```

---

## 7. Base ViewModel Classes

Location: `presentation/core/BaseViewModel.kt`

```kotlin
abstract class BaseViewModel : ViewModel() {
    private val _loadingSet = MutableStateFlow<Set<String>>(emptySet())
    val loading = _loadingSet.map { it.isNotEmpty() }

    private val _snackBarData = Channel<SnackBarData>()
    val snackBarData = _snackBarData.receiveAsFlow()

    fun launch(
        context: CoroutineContext = EmptyCoroutineContext,
        actionId: String = UUID.randomUUID().toString(),
        loadingEnabled: Boolean = false,
        block: suspend CoroutineScope.() -> Unit,
    ): Job {
        // Implementation with error handling
    }
}

abstract class StateViewModel<S>(initialState: S) : BaseViewModel() {
    protected val mutableState = MutableStateFlow(initialState)
    val state = mutableState.asStateFlow()

    protected inline fun updateState(block: S.() -> S) {
        mutableState.update(block)
    }
}
```

---

## 8. Navigation Pattern

### 8.1 Screen Definitions

Location: `presentation/navigation/controller/Screens.kt`

```kotlin
@Stable
@Serializable
sealed interface Screens : NavKey {

    @Serializable
    data object Splash : Screens

    @Serializable
    sealed interface Employer : Screens {
        @Serializable
        data object CreateProfile : Employer

        @Serializable
        data object Main : Employer
    }
}
```

### 8.2 Entry Definitions

Location: `presentation/navigation/controller/{Feature}Entry.kt`

```kotlin
fun EntryProviderScope<Screens>.employerEntry(
    controller: NavigationController,
) {
    entry<Screens.Employer.CreateProfile> {
        CreateEmployerProfileScreen(
            onHome = { controller.replaceAll(Screens.Seeker.Main) }
        )
    }
}
```

---

## 9. Dependency Injection (Koin)

### 9.1 Annotations

| Annotation | Usage |
|------------|-------|
| `@KoinViewModel` | ViewModels |
| `@Single` | Singletons (repositories, data sources) |
| `@Factory` | Use cases (new instance each time) |
| `@Named("authenticated")` | Authenticated HTTP client |

### 9.2 Binding Pattern

```kotlin
@Single(binds = [EmployerRepository::class])
class DefaultEmployerRepository(...) : EmployerRepository
```

---

## 10. Key Extensions

### 10.1 Core Extensions

Location: `core/extension/`

```kotlin
// Flow.kt - Side effect creation
fun <T> mutableSideEffect() = MutableSharedFlow<T>(
    extraBufferCapacity = 2,
    onBufferOverflow = BufferOverflow.DROP_OLDEST
)
```

### 10.2 Presentation Extensions

Location: `presentation/core/extension/`

```kotlin
// TextData.kt
fun String?.toTextData(): TextData = TextData.Text(value = this ?: "")
fun @receiver:StringRes Int.toTextData(): TextData = TextData.Resource(resId = this)

// Flow.kt
@Composable
fun <T> Flow<T>.collectAsEffect(action: suspend (T) -> Unit) {
    LaunchedEffect(Unit) {
        collect(action)
    }
}
```

---

## 11. UI Components

Location: `presentation/core/components/base/`

| Component | Description |
|-----------|-------------|
| `Screen<VM>` | Wrapper for ViewModel injection with loading/snackbar |
| `FullscreenColumn` | Standard full-screen layout with padding |
| `NotDjinniTextField` | Custom text field with consistent styling |
| `NotDjinniButton` | Custom button component |
| `NotDjinniText` | Text component with TextData support |
| `VerticalSpacer` | Vertical spacing |
| `HorizontalSpacer` | Horizontal spacing |
| `MessageCard` | Error/info message display |

---

## 12. Request/Response DTOs

### 12.1 Request Pattern

```kotlin
@Serializable
data class CreateSeekerProfileRequest(
    @SerialName("speciality")
    val speciality: String,
    @SerialName("desired_salary")
    val desiredSalary: Int,
)
```

### 12.2 Response Pattern

```kotlin
@Serializable
data class EmployerProfileResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("role")
    val role: String,
    @SerialName("company")
    val company: CompanyResponse
)

@Serializable
data class CompanyResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("company_name")
    val companyName: String,
    @SerialName("website")
    val website: String?,
    @SerialName("description")
    val description: String
)
```

---

## 13. Domain Models

Location: `model/`

```kotlin
// model/employer/EmployerProfile.kt
data class EmployerProfile(
    val id: Long,
    val role: String,
    val company: Company
)

// model/company/Company.kt
data class Company(
    val id: Long,
    val name: String,
    val website: String?,
    val description: String
)
```

---

## 14. Overlay/Alert Pattern (Inner Content)

For screens with overlays (like company selection), use AnimatedContent:

```kotlin
AnimatedContent(
    targetState = state.currentAlert == CreateProfileAlert.SELECT_COMPANY,
    transitionSpec = { fadeIn() togetherWith fadeOut() }
) { isAlertVisible ->
    if (isAlertVisible) {
        SelectCompanyContent(onAction = viewModel::sendAction)
    } else {
        Content(state = state, onAction = viewModel::sendAction)
    }
}

BackHandler(enabled = state.currentAlert != null) {
    viewModel.sendAction(CreateEmployerProfileAction.HideAlert)
}
```

---

## 15. File Naming Conventions

| Type | Pattern | Example |
|------|---------|---------|
| Data Source Interface | `{Feature}DataSource.kt` | `CompanyDataSource.kt` |
| Data Source Impl | `Default{Feature}DataSource.kt` | `DefaultCompanyDataSource.kt` |
| Repository Interface | `{Feature}Repository.kt` | `CompanyRepository.kt` |
| Repository Impl | `Default{Feature}Repository.kt` | `DefaultCompanyRepository.kt` |
| Request DTO | `{Operation}Request.kt` | `CreateEmployerProfileRequest.kt` |
| Response DTO | `{Model}Response.kt` | `CompanyResponse.kt` |
| Resource | `{Feature}.kt` | `Company.kt` |
| Mapper | `{Model}Mapper.kt` | `CompanyMapper.kt` |
| ViewModel | `{Feature}ViewModel.kt` | `CreateEmployerProfileViewModel.kt` |
| State | `{Feature}State.kt` | `CreateEmployerProfileState.kt` |
| Action | `{Feature}Action.kt` | `CreateEmployerProfileAction.kt` |
| SideEffect | `{Feature}SideEffect.kt` | `CreateEmployerProfileSideEffect.kt` |
| Alert Enum | `{Feature}Alert.kt` | `CreateEmployerProfileAlert.kt` |

---

## 16. Validation Pattern

Validation in ViewModel:

```kotlin
private fun CreateSeekerProfileAction.CreateProfile.getValidationErrorResId(): Int? {
    return when {
        speciality.isBlank() -> R.string.speciality_should_not_be_empty
        yearsOfExperience.isBlank() -> R.string.years_of_experience_should_not_be_empty
        desiredSalary.isBlank() -> R.string.desired_salary_should_not_be_empty
        else -> null
    }
}
```

---

## 17. Message Display Pattern

Using Channel for temporary messages:

```kotlin
private val messages = Channel<TextData>(capacity = Channel.UNLIMITED)

init {
    collectMessages()
}

private fun collectMessages() {
    launch {
        messages.consumeAsFlow().collect { message ->
            updateState { copy(message = message) }
            delay(MESSAGE_DISPLAY_DURATION_MS)
            updateState { copy(message = null) }
        }
    }
}

// Usage
messages.trySend(TextData.Resource(R.string.error_message))
```
