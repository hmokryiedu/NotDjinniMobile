## Правила реализации Android

### Источники проекта
- App-модуль: `:app`.
- Feature-модули: нет отдельных Gradle modules; features живут в `app/src/main/kotlin/not/djinni/presentation/screens/...`.
- Core/shared-модули: packages внутри app: `core`, `domain`, `data`, `network`, `model`, `datastore`, `di`, `utils`.
- Build-файлы: `settings.gradle.kts`, root `build.gradle.kts`, `app/build.gradle.kts`, `gradle/libs.versions.toml`.
- Расположение тестов: local JVM -> `app/src/test`; instrumented -> `app/src/androidTest`; сейчас файлов нет.

### Конвенции реализации
- Именование: screen flow держит `*Screen`, `*ViewModel`, `*State`, `*Action`, `*SideEffect`; data impl `Default*Repository`/`Default*DataSource`; mapping через `toDomain()`/`toResponse()`.
- Стиль: UI action -> `ViewModel.sendAction()` -> private method -> usecase -> repository -> datasource; state через `StateViewModel`; one-shot events через side effects.
- Kotlin calls с 2+ non-lambda аргументами писать с named args.
- ViewModels, наследующие `BaseViewModel` или `StateViewModel`, запускают coroutines через protected `launch(...)`; прямой `viewModelScope.launch` допустим только внутри base class.
- DI/wiring: Koin annotations `@KoinViewModel`, `@Factory`, `@Single(binds = [...])`; manual module binding только для named/client/datastore-like объектов.
- Не добавлять DI для static compile-time constants.
- Navigation wiring: route в sealed `Screens`; entry в соответствующий `*Entry.kt`; navigation callbacks из entry в screen.
- Ошибки: network возвращает `NetworkResponse`; repository/usecase оборачивает в `Result`/`runCatching`; ViewModel делает `onSuccess/onFailure`, snackbar/sideEffect/state; не вводить broad catch/wrapper error policy без плана.
- Generated files: не редактировать `build/`, KSP output, compose compiler reports, IDE/cache outputs.

### Команды
- Build: `./gradlew :app:assembleDebug` или быстрый чек `./gradlew :app:compileDebugKotlin`.
- Unit tests: `./gradlew :app:testDebugUnitTest` после добавления/изменения local JVM tests.
- UI tests: `./gradlew :app:connectedDebugAndroidTest` только если есть device/emulator и androidTest.
- Lint/format: `./gradlew :app:lintDebug`; ktlint/detekt/spotless policy не найдена.

### Разрешённые изменения
- Менять минимальный набор файлов нужного слоя.
- Для новой feature-flow добавлять screen/state/action/sideEffect/ViewModel/usecase/repository/datasource/resource/request/response/model/mapper только если реально нужны.
- Обновлять DI/navigation wiring в существующих точках.
- Добавлять dependencies через version catalog только при прямой необходимости.
- Делать minimal compile fixes только если они вызваны текущим diff.

### Запрещённые изменения
- Не создавать новые Gradle modules без отдельного решения.
- Не дублировать UI/theme/design-system rules.
- Не дублировать architecture rules про boundaries сверх нужного wiring.
- Не редактировать generated/build outputs.
- Не вводить Hilt/Dagger/manual service locator.
- Не добавлять абстракции, config, fallback logic или broad error handling без запроса.

### Стоп-правила
- Спросить, если: нужен новый Gradle module; нужна новая dependency; неясен обязательный verification command; задача требует generated file edit; есть конфликт pattern vs запрос.
- Вернуть в research, если: нет похожего implementation path; изменение затрагивает ownership core/shared packages; navigation flow неясен; API contract не задан.

### Evidence
- Просмотренные файлы: `AGENTS.md`, `settings.gradle.kts`, `build.gradle.kts`, `app/build.gradle.kts`, `gradle/libs.versions.toml`, `stability_config.conf`, `AppModule.kt`, `NetworkModule.kt`, `NotDjinniApplication.kt`, `BaseViewModel.kt`, `AuthViewModel.kt`, `CreateVacancyViewModel.kt`, `CreateVacancyUseCase.kt`, `DefaultVacancyRepository.kt`, `DefaultVacancyDataSource.kt`, `NetworkResponse.kt`, `HttpResponse.kt`, `NavigationController.kt`, `NotDjinniNavDisplay.kt`, `Screens.kt`, `SeekerEntry.kt`, `EmployerEntry.kt`.
- Заметки по confidence: high для modules/build/DI/navigation/naming/style; medium для lint/format, потому что explicit policy files отсутствуют.
