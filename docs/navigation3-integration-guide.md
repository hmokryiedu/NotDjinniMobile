# Navigation3 Integration Guide

This guide describes a reusable Navigation3 integration pattern for Compose apps.

## Core setup

- Routes: `AppNameRoute` is a typed `@Serializable` `NavKey` hierarchy.
- Stack owner: `NavigationController` owns one active stack from `rememberNavBackStack`.
- Host: `AppNameNavDisplay` renders the stack with `NavDisplay` and stable decorators:
  - `rememberSaveableStateHolderNavEntryDecorator()`
  - `rememberViewModelStoreNavEntryDecorator()`
  - `rememberResultEventBusNavEntryDecorator()`
- Results: use official result bus primitives only (`ResultEffect`, `LocalResultEventBus`).

## Typed route keys

```kotlin
@Serializable
sealed interface AppNameRoute : NavKey {
    @Serializable
    data object Auth : AppNameRoute

    @Serializable
    sealed interface Feature : AppNameRoute {
        @Serializable
        data class Details(val itemId: Long) : Feature
    }
}
```

If a flow needs a return contract, keep explicit stable fields in the route or derived keys.

## Back stack and controller wrapper

```kotlin
@Composable
fun rememberNavigationController(
    startRoute: Screens = Screens.Splash,
    onRootBack: () -> Unit = {}
): NavigationController {
    val stack = rememberNavBackStack(startRoute) as NavBackStack<Screens>
    return remember(stack, onRootBack) { NavigationController(stack, onRootBack) }
}

class NavigationController(
    val stack: NavBackStack<Screens>,
    private val onRootBack: () -> Unit
) {
    fun navigate(route: Screens) = stack.add(route)

    fun popBackStack(): Boolean {
        if (stack.size > 1) {
            stack.removeLastOrNull()
            return true
        }
        onRootBack()
        return false
    }

    fun replaceAll(route: Screens) {
        stack.clear()
        stack.add(route)
    }

    fun popUpTo(key: Screens, to: Screens, inclusive: Boolean = false): Boolean {
        val index = stack.lastIndexOf(to)
        if (index == -1) return false

        val removeFrom = if (inclusive) index else index + 1
        if (removeFrom < stack.size) {
            for (stackIndex in stack.lastIndex downTo removeFrom) {
                stack.removeAt(stackIndex)
            }
        }
        if (stack.isEmpty()) stack.add(to)
        stack.add(key)
        return true
    }
}
```

Invariants:
- `navigate` appends;
- `popBackStack` returns `true` only when one item is removed; otherwise calls `onRootBack` and returns `false`;
- `replaceAll` clears stack and leaves one route;
- `popUpTo` returns `false` when `to` is absent; otherwise trims stack, keeps it non-empty, then pushes `key`;
- one owner per active stack;
- active stack must never become empty.

## NavDisplay + entry providers

Split entries by feature area and compose them in one provider (for example `authEntries`, `profileEntries`, `settingsEntries`).

```kotlin
@Composable
fun AppNameNavDisplay(controller: NavigationController) {
    NavDisplay(
        backStack = controller.stack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
            rememberResultEventBusNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            authEntries(controller)
            profileEntries(controller)
            settingsEntries(controller)
            entry<AppNameRoute.Feature.Details> { route ->
                FeatureDetailsScreen(
                    itemId = route.itemId,
                    onBack = controller::popBackStack
                )
            }
        }
    )
}
```

Rules:
- key type and `entry<...>` type must match 1:1;
- pass navigation callbacks through composable boundaries;
- keep feature ownership inside feature entry providers.

## Result bus pattern

Consumer:

```kotlin
ResultEffect<String>(resultKey) { value ->
    viewModel.onResult(value)
}
```

Producer:

```kotlin
val resultEventBus = LocalResultEventBus.current
resultEventBus.sendResult(resultKey, payload)
```

Guidelines:
- generate deterministic result keys per flow;
- keep one producer and one consumer contract per key;
- keep payload typed and minimal.

## ViewModel side-effect boundary

- ViewModel emits side effects (`NavigateBack`, `OpenDetails(id)`, etc.).
- Composable collects side effects and calls `NavigationController`.
- ViewModel does not depend on navigation controller types and does not mutate back stack directly.

## Best practices

- Use typed `NavKey` models only; avoid string routes.
- Keep one owner for each active stack.
- Keep stack non-empty while `NavDisplay` is mounted.
- Keep navigation execution in UI boundary, triggered by ViewModel side effects.
- Use `ResultEffect` and `LocalResultEventBus` before designing custom result stores.
- Keep Navigation3 dependency/version changes out of feature work unless explicitly required.

## Avoid list

- String-based or untyped route maps.
- Multiple unsynchronized owners of the same active stack.
- Empty-stack states during active navigation rendering.
- Direct ViewModel navigation calls to controller APIs.
- Custom result stores when official result bus fits the flow.
- Manual save/restore logic that replaces Navigation3 state APIs such as `rememberNavBackStack`.
- Dependency/version changes without explicit need.
