## Theme

- UI platform: Android Kotlin with Jetpack Compose.
- App theme source: `not.djinni.presentation.theme.NotDjinniTheme`.
- Wrap Compose UI in `NotDjinniTheme { ... }` for previews and app-level content.
- Use theme accessors instead of raw literals:
  - `NotDjinniTheme.colors`
  - `NotDjinniTheme.typography`
  - `NotDjinniTheme.offsets`
  - `NotDjinniTheme.shapes`
- Color tokens live in `app/src/main/kotlin/not/djinni/presentation/theme/NotDjinniColor.kt`.
- Typography tokens live in `app/src/main/kotlin/not/djinni/presentation/theme/NotDjinniTypography.kt`.
- Spacing tokens live in `app/src/main/kotlin/not/djinni/presentation/theme/NotDjinniOffset.kt`.
- Shape tokens live in `app/src/main/kotlin/not/djinni/presentation/theme/NotDjinniShape.kt`.
- Icon entrypoint: `not.djinni.presentation.theme.NotDjinniIcons`.
- Custom icon vectors live under `app/src/main/kotlin/not/djinni/presentation/theme/icons/`.
- Font family is e-Ukraine from `app/src/main/res/font/e_ukraine_*.otf`.
- Default app surface is dark: `NotDjinniTheme.colors.background`.
- App entrypoint applies theme in `app/src/main/kotlin/not/djinni/presentation/MainActivity.kt`.
- Navigation shell is `NotDjinniNavDisplay` with Navigation3 fade transitions.

## Rules for writing UI

- Use existing base components before creating new UI primitives.
- Use `NotDjinniText` for text rendering.
- Use `TextData` and `toTextData()` for text values and string resources.
- Use `NotDjinniButton` for primary actions.
- Use `OutlinedNotDjinniButton` for outlined actions.
- Use `ButtonData` for button text, enabled state, loading state, and optional icons.
- Use `NotDjinniTextField` for text input.
- Use `buildDefaultTextFieldDecorator` when a custom text field decorator is needed.
- Use `FullscreenColumn` for full-screen screen layouts.
- Use `buildFullscreenColumnPadding` for screen padding variants.
- Use `VerticalSpacer` and `HorizontalSpacer` with `NotDjinniTheme.offsets`.
- Use `VacancyCard` for vacancy list rows.
- Use `ApplicationCard` for application list rows.
- Use `NotDjinniTabBar` for seeker vacancy tabs.
- Use `NotDjinniLoader` for loading indicators.
- Use `MessageCard` for animated inline message cards.
- Use `AlertContainer` before showing alert/sheet UI that depends on lifecycle state.
- Use `ModalBottomSheet` with `containerColor = NotDjinniTheme.colors.surface` and `contentColor = NotDjinniTheme.colors.onSurface`.
- Use `clickableNoRipple` for project-style clickable elements.
- Use `performWithTimeout` through existing button components; do not duplicate double-click protection.
- Use `Screen<VM>` wrapper for ViewModel-backed screens so loading overlay and snackbar behavior stay consistent.
- Render screen state as explicit loading, error, empty, and data branches where state model supports it.
- Put list content in `LazyColumn`; use stable item keys when item ids exist.
- Use `Modifier.imePadding()` on list/form areas affected by keyboard.
- Use `systemBarsPadding()` through `FullscreenColumn` unless screen has a proven custom inset need.
- Use `navigationBarsPadding()` inside bottom sheet content.
- Use `NotDjinniTheme.colors.error` for validation and blocking error text.
- Use `NotDjinniTheme.colors.success` or application status colors for positive/status indicators.
- Use alpha on theme colors for secondary text, placeholder text, disabled text, and subdued surfaces.
- Use decorative icons with `contentDescription = null`.
- Give meaningful `contentDescription` only to icons/images that communicate information not present in nearby text.
- Add a Compose `@Preview` for every new reusable component and every substantial screen content function.
- Wrap previews in `NotDjinniTheme`.
- Prefer `ScreenSizesPreview` for new screens where layout can change across phone/tablet sizes.

## Best practices

- Keep screens split into public route composable plus private stateless `Content(...)`.
- Keep ViewModel collection at route level with `collectAsStateWithLifecycle`.
- Keep side effects at route level using `collectAsEffect`.
- Pass user events upward through `onAction`.
- Keep private UI helpers close to the screen when they are screen-specific.
- Promote UI to `presentation/core/components/base` only when reused across screens.
- Keep cards full-width, bordered with `NotDjinniTheme.colors.onSurface`, `NotDjinniTheme.shapes.small`, and `NotDjinniTheme.colors.primary.copy(alpha = 0.3f)` when matching existing vacancy/application cards.
- Use `NotDjinniTheme.offsets.medium` for common inner padding.
- Use `NotDjinniTheme.offsets.small` or `tiny` for compact vertical rhythm inside cards.
- Use `title1Bold` for main list screen headings.
- Use `title2.copy(fontWeight = FontWeight.Bold)` for detail/create top bars.
- Use `body3` for form labels.
- Use `body1` for primary input text.
- Use `body2` and alpha-reduced `onBackground`/`onSurface` for secondary details.
- Use `highlightedContainer` for selected rows and highlighted information panels.
- Use bottom fixed action areas for primary submit/apply actions on long forms/details.
- Use centered text for empty list states.
- Use `NotDjinniLoader` centered in full-size containers for screen loading.
- Use existing project icons through `NotDjinniIcons`; use Material icons only when no project icon exists.

## Stop Rules

- Ask before introducing a new design system dependency.
- Do not replace `NotDjinniTheme` with MaterialTheme tokens.
- Ask before redesigning `NotDjinniButton`; file currently contains a TODO, but it is still the canonical primary button.
- Ask before adding new token names to `NotDjinniColor`, `NotDjinniTypography`, `NotDjinniOffset`, or `NotDjinniShape`.
- Ask before adding a new base component if existing components can be composed.
- Ask before changing app-wide font scaling behavior in `MainActivity.attachBaseContext`.
- Ask before changing Navigation3 transition timing or navigation shell behavior.
- Ask before adding screenshot/UI test requirements; no existing UI test setup was found.
- Do not hardcode colors, spacing, radii, or typography in new UI unless matching a local one-off pattern already present in the touched file.
- Do not add visible instructional text about how to use UI controls unless product copy already requires it.
- Do not create XML View UI; this project UI evidence is Compose.
