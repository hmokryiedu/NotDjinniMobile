# Navigation3 Integration in NotDjinni

## Dependency matrix

Source: `/Users/hlibmokryi/Development/Mobile/Android/University/NotDjinni/gradle/libs.versions.toml`, `/Users/hlibmokryi/Development/Mobile/Android/University/NotDjinni/app/build.gradle.kts`.

- `androidx.navigation3:navigation3-runtime` (`libs.navigation.runtime`, version `navigation-core = 1.0.0`)
- `androidx.navigation3:navigation3-ui` (`libs.navigation.ui`, version `navigation-core = 1.0.0`)
- `androidx.lifecycle:lifecycle-viewmodel-navigation3` (`libs.androidx.lifecycle.viewmodel.navigation`, version `lifecycle-runtime = 2.10.0`)
- `org.jetbrains.kotlinx:kotlinx-serialization-json` (`libs.kotlinx.serialization.json`) used for stack serialization

`app/build.gradle.kts` wires these in `dependencies { implementation(...) }`.

## Root setup from `MainActivity`

Source: `/Users/hlibmokryi/Development/Mobile/Android/University/NotDjinni/app/src/main/kotlin/not/djinni/presentation/MainActivity.kt`.

- `MainActivity.onCreate` creates controller with `rememberNavigationController(Screens.Splash)`.
- UI root renders `NotDjinniNavDisplay(controller = controller)` as app navigation host.
- Initial route is `Screens.Splash`.

## Route model (`Screens`)

Source: `/Users/hlibmokryi/Development/Mobile/Android/University/NotDjinni/app/src/main/kotlin/not/djinni/presentation/navigation/controller/Screens.kt`.

- Route type is `@Serializable sealed interface Screens : NavKey`.
- Top-level routes: `Splash`, `Auth`, `ChooseRole`.
- Nested route groups:
  - `Screens.Public`: `Main`, `VacancyDetails(vacancyId: Long)`
  - `Screens.Seeker`: main/profile/vacancy/application flows + typed args
  - `Screens.Employer`: main/profile/vacancy/application/create flows + typed args
- All concrete routes are `@Serializable`, so JSON save/restore is supported.

## Custom `NavigationController`

Source: `/Users/hlibmokryi/Development/Mobile/Android/University/NotDjinni/app/src/main/kotlin/not/djinni/presentation/navigation/NavigationController.kt`.

State model:
- Back stack: `private val _stack = mutableStateListOf(initialKey)`
- Public stack: `val stack: SnapshotStateList<Screens>`
- Results storage: `private val navResultStore = NavResultStore()`

Persistence (`rememberNavigationController`):
- Uses `rememberSaveable` + `listSaver`.
- Saves two payloads:
  - `it.stack.map(Json::encodeToString)` (route list as JSON strings)
  - `it.navResultStore.snapshot().toList()` (pending nav results)
- Restores by `Json.decodeFromString<Screens>(...)` and repopulates `navResultStore`.

Behavior:
- `navigate(key)`: push new key.
- `popBackStack()`: `removeLastOrNull()` (no crash on empty).
- `replaceAll(key)`: clear stack and set single root.
- `popUpTo(key, to, inclusive)`: remove to target index, then push `key`.
- `navigateForResult(key, resultKey)`: currently same as `navigate(key)`.
- `popWithResult(resultKey, value)`: encode via contract, store, then pop.
- `consumeResult(resultKey)`: consume encoded value and decode via contract.

## `NavResult.kt` design

Source: `/Users/hlibmokryi/Development/Mobile/Android/University/NotDjinni/app/src/main/kotlin/not/djinni/presentation/navigation/NavResult.kt`.

Components:
- `NavResultContract<T>`: `encode(value: T): String`, `decode(value: String): T`.
- `NavResultKey<T>`: typed key pair of `id: String` + `contract`.
- `NavResultStore`: internal `MutableMap<String, String>`.

Semantics:
- Encoded `String` map only.
- One pending result per `id`.
- Last write wins (`put` overwrites).
- `consume(id)` removes and returns value (single-consume).

Controller wiring:
- Writer path: `popWithResult(...)` -> `navResultStore.put(...)` -> `popBackStack()`.
- Reader path: `consumeResult(...)` -> `navResultStore.consume(...)` -> contract decode.

## `NotDjinniNavDisplay` host

Source: `/Users/hlibmokryi/Development/Mobile/Android/University/NotDjinni/app/src/main/kotlin/not/djinni/presentation/navigation/NotDjinniNavDisplay.kt`.

- Uses `NavDisplay`.
- `backStack = controller.stack`.
- `onBack = controller::popBackStack`.
- `entryProvider` composed from 5 domain providers:
  - `authEntry`, `employerEntry`, `publicEntry`, `seekerEntry`, `splashEntry`
- Entry decorators:
  - `rememberSaveableStateHolderNavEntryDecorator()`
  - `rememberViewModelStoreNavEntryDecorator()`
- Transitions: same fade in/out tween (`ANIMATION_DURATION = 600`) for normal/pop/predictive-pop.
- No custom `sceneStrategy` configured (defaults are used).

## Entry provider split by domain

Sources:
- `/Users/hlibmokryi/Development/Mobile/Android/University/NotDjinni/app/src/main/kotlin/not/djinni/presentation/navigation/controller/AuthEntry.kt`
- `/Users/hlibmokryi/Development/Mobile/Android/University/NotDjinni/app/src/main/kotlin/not/djinni/presentation/navigation/controller/SplashEntry.kt`
- `/Users/hlibmokryi/Development/Mobile/Android/University/NotDjinni/app/src/main/kotlin/not/djinni/presentation/navigation/controller/PublicEntry.kt`
- `/Users/hlibmokryi/Development/Mobile/Android/University/NotDjinni/app/src/main/kotlin/not/djinni/presentation/navigation/controller/SeekerEntry.kt`
- `/Users/hlibmokryi/Development/Mobile/Android/University/NotDjinni/app/src/main/kotlin/not/djinni/presentation/navigation/controller/EmployerEntry.kt`

Pattern:
- Each file defines `fun EntryProviderScope<Screens>.<domain>Entry(controller: NavigationController)`.
- Each `entry<ScreenType>` maps route -> screen composable and delegates navigation to controller methods.

## ViewModel/state integration

Sources:
- `/Users/hlibmokryi/Development/Mobile/Android/University/NotDjinni/app/src/main/kotlin/not/djinni/presentation/core/Screen.kt`
- `/Users/hlibmokryi/Development/Mobile/Android/University/NotDjinni/app/src/main/kotlin/not/djinni/presentation/screens/seeker/vacancy/details/VacancyDetailsScreen.kt`

`Screen.kt`:
- `Screen<VM>()` wrapper uses `koinViewModel<VM>()`.
- Parametrized overload `Screen<VM>(key, parameters)` passes Koin `ParametersHolder`.
- Shared `Content(...)` overlays loading + snackbar via `BaseViewModel` flows.

Example (`VacancyDetailsScreen`):
- Creates `NavResultKey<String>` per vacancy id (`cover_letter_result_$vacancyId`).
- Injects `VacancyDetailsViewModel` with `Screen<VacancyDetailsViewModel>(parameters = { parametersOf(vacancyId) })`.
- Result flow:
  - Consumes `coverLetterResult` from navigation layer.
  - Applies it in `LaunchedEffect(coverLetterResult)` via `VacancyDetailsAction.ApplyCoverLetterTemplate`.
  - Emits side effect `NavigateToCoverLetterTemplates(resultKeyId)`; screen calls navigation lambda with typed `NavResultKey`.

## Current constraints / gaps

- No dedicated Navigation3 integration/unit tests found for controller, stack persistence, or nav results.
- `navigateForResult` does not register/validate pending requests; it is currently plain `navigate`.
- Result ids must be unique by flow context; collisions overwrite pending value.
- `consumeResult` is one-shot; recomposition after consume gets `null`.
- `popBackStack` allows empty stack state if repeatedly invoked.
- `NavDisplay` scene behavior is default (no custom scene strategy tuning).
