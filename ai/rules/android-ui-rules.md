# Правила UI Android

## Источники проекта
- App module: `:app`.
- Feature modules: отдельных feature modules нет; UI по package `app/src/main/kotlin/not/djinni/presentation/screens`.
- Design System: `app/src/main/kotlin/not/djinni/presentation/theme` и `app/src/main/kotlin/not/djinni/presentation/core/components`.
- Theme/tokens: `NotDjinniTheme.colors`, `NotDjinniTheme.typography`, `NotDjinniTheme.offsets`, `NotDjinniTheme.shapes`.
- Компоненты: `NotDjinniText`, `NotDjinniButton`, `OutlinedNotDjinniButton`, `NotDjinniTextField`, `FullscreenColumn`, `NotDjinniLoader`, `VacancyCard`, `ApplicationCard`, `NotDjinniTabBar`.
- Screens: `presentation/screens/**`.
- Tests: UI tests не найдены; использовать previews из `presentation/core/Preview.kt` и локальные `@Preview`.
- Similar screens/features: auth form, vacancy/application lists/details, create vacancy/profile flows.

## UI-структура проекта
- Compose style: screen entry `Screen<VM>`, state через `collectAsStateWithLifecycle`, UI body в private `Content`.
- Component ownership: shared UI в `presentation/core/components`; screen-specific UI рядом со screen package. Новый shared component добавлять только если он реально reused/generic; иначе держать screen-local.
- Token ownership: цвета, типографика, spacing, shapes только в `presentation/theme`.
- Layout patterns: `FullscreenColumn` для fullscreen screens; `LazyColumn` для списков; `VerticalSpacer`/`HorizontalSpacer` для gaps; списки используют stable keys.
- State rendering: для async content использовать sealed state `Loading/Error/Empty/Data`; global loading/snackbar уже есть в `Screen.kt`.
- Interaction callbacks: screen отдаёт события через `onAction(Action)`; navigation через side effects.
- Preview/test pattern: каждый новый component/screen content получает `@Preview` внутри `NotDjinniTheme`.

## Project rules
- Compose: не писать UI logic в ViewModel-less composable; screen entry только связывает VM, state, side effects.
- Design System: сначала искать готовый `NotDjinni*` component; новый shared component класть в `presentation/core/components`.
- Компоненты: текст через `NotDjinniText`; кнопки через `NotDjinniButton`/`OutlinedNotDjinniButton`; поля через `NotDjinniTextField`; cards расширять от существующих patterns.
- Токены: не hardcode spacing/shape/text style; использовать `NotDjinniTheme`. Локальные `dp` допустимы для icon size/indicator size как private const/val.
- Layout: fullscreen screen строить через `FullscreenColumn`; scroll/ime/system bars брать из существующих helpers.
- States: loading показывать `NotDjinniLoader`; error через `NotDjinniTheme.colors.error`; empty отдельным content block; retry через `NotDjinniButton`; popup/dialog/bottom sheet state держать одним nullable полем `popup: Popup?`, а не boolean flags.
- Modal state: варианты popup/dialog/sheet моделировать `sealed interface`, если варианты несут данные, или `enum`, если данных нет; закрытие делать через `popup = null`.
- Interactions: click через `clickableNoRipple` или component callback; submit/click throttling через существующий `performWithTimeout` в shared buttons.
- Motion: `AnimatedContent` и другие animations использовать только если есть похожий локальный pattern для такого state transition.
- Accessibility: для decorative icons `contentDescription = null`; для actionable icon-only controls добавлять description, если нет visible text рядом.
- Figma/assets: если Figma дан, сверять с ним; если нет, code DS source of truth. Если Figma конфликтует с DS tokens/components, остановиться и спросить. Не вводить новый visual language без evidence.
- Tests: добавлять/обновлять `@Preview` для нового UI; screenshot/UI tests не добавлять без существующего project pattern или явного запроса.

## Стоп-правила
- Спросить, если: нужен внешний Figma как source of truth; найден конфликт tokens/components vs Figma; непонятно, shared component или screen-local; задача просит новый DS token.
- Вернуть в research, если: похожий экран незавершён; нет evidence для нужного component; изменение требует править core DS conventions.

## Evidence
- Просмотренные файлы: `settings.gradle.kts`, `app/build.gradle.kts`, `gradle/libs.versions.toml`, `AGENTS.md`, `app/src/main/kotlin/not/djinni/presentation/theme/*.kt`, `app/src/main/kotlin/not/djinni/presentation/core/Screen.kt`, `Preview.kt`, `presentation/core/components/base/*.kt`, `AuthScreen.kt`, `CreateVacancyScreen.kt`, `ViewVacancyApplicationsScreen.kt`, `AllVacanciesScreen.kt`.
- Заметки по confidence: high для modules/theme/components/layout/actions; medium для state canonical pattern из-за смешения sealed state и boolean flags.
