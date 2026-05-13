# Правила архитектуры Android

## Project Source Paths
- App module: `:app`
- Feature modules: none; features live under `app/src/main/kotlin/not/djinni/presentation/screens/**`
- Navigation: `app/src/main/kotlin/not/djinni/presentation/navigation/**`
- DI: `app/src/main/kotlin/not/djinni/di/**`
- Data/network/storage: `data/**`, `network/**`, `datastore/**`
- Tests: `app/src/test`, `app/src/androidTest`
- Similar features: auth, seeker, employer screens under `presentation/screens/**`

## Project Architecture
- Style: package-layered architecture inside single app module.
- Module ownership: `:app` owns application, UI, domain, data, network, storage.
- Presentation owner: Compose screen + screen ViewModel.
- State owner: ViewModel owns state via `StateViewModel<S>` and `MutableStateFlow`.
- Navigation owner: `presentation/navigation/controller/*Entry.kt` wires `Screens` to screens.
- DI/wiring: Koin annotations with `AppModule().module`; manual Koin modules only for shared wiring.
- Data/domain boundary: domain defines repository interfaces/use cases; data implements repositories; network/datastore stay behind data sources or storage interfaces.

## Project Rules
- Presentation: new screens in `presentation/screens/<area>/<feature>/`; keep `*State`, `*Action`, `*SideEffect`, `*ViewModel`, `*Screen` together; use `Screen<FeatureViewModel>`; UI calls `sendAction`.
- ViewModel: `init` blocks contain only named private function calls; Flow collection, branching, state updates, and business logic live in those functions.
- State/events/effects: use `StateViewModel<State>`; expose immutable `state`; update through `updateState`/`mutableState.update`; use sealed `Action`; side effects for navigation/one-shot events; collect via `collectAsEffect`; modal state is one nullable field such as `popup: Popup?`, hidden by setting it to `null`.
- Navigation: add route keys to `Screens`; wire destination in relevant `*Entry.kt`; pass callbacks from entries into screens; use `NavigationController`; navigation decisions go through side effects/callbacks, not direct business decisions in UI.
- DI: prefer Koin annotations `@KoinViewModel`, `@Single`, `@Factory`; bind with `binds`; use `@Named` for same-type deps; manual providers only for framework/shared named deps.
- Data/network/storage: domain repository interfaces must not depend on Ktor/DataStore/DTO/Android storage APIs; repositories translate network/DTO to domain `Result`; network calls in `network/**DataSource`; preferences behind `datastore/**`; no Room rules until real Room code exists.
- Tests: local JVM tests in `app/src/test`; add test deps only when first real tests introduced; prefer ViewModel/usecase/repository tests with fakes.

## Stop Rules
- Ask if: adding new Gradle/feature module; introducing Room DB/DAO/entity architecture; changing Navigation3 ownership; replacing Koin annotations; runtime args need factory/route contract and the contract is absent.
- Return to research if: feature does not match screen/state/action/side-effect pattern; dependency direction unclear; tests need project-wide framework choices; public contract changes affect multiple features/layers; state owner is unclear.

## Evidence
- Scanned files: `settings.gradle.kts`, `build.gradle.kts`, `app/build.gradle.kts`, `gradle/libs.versions.toml`, `BaseViewModel.kt`, `Screen.kt`, navigation controller/entries, `NotDjinniApplication.kt`, `di/*Module.kt`, `domain/**`, `data/**`, `network/**`, `datastore/**`, `app/src/test`, `app/src/androidTest`.
- Confidence notes: high for single-module architecture, presentation, state, navigation, DI, data boundaries; medium for storage future rules because Room deps exist but implementation absent.
