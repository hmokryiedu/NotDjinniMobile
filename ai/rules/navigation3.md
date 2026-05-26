# Navigation3 Invariants

## Routes

- `Screens` is `@Serializable sealed interface Screens : NavKey`.
- Routes are plain keys. No `parentRoute` ownership model.

## Controller state

- Navigation state is one `NavBackStack<Screens>` (`NavigationController.stack`).
- Initial stack key is `Screens.Splash`.
- Active back stack passed to `NavDisplay` is always `controller.stack`.

## Navigation behavior

- `navigate(route)` appends `route` to current stack.
- `replaceAll(route)` clears stack and sets exactly `[route]`.
- `popBackStack()`:
  - pops last key when size > 1 and returns `true`;
  - calls `onRootBack` when size == 1 and returns `false`.
- `popUpTo(key, to, inclusive)`:
  - returns `false` and does not mutate state when `to` is missing;
  - otherwise removes range according to `inclusive`, keeps stack non-empty, then appends `key`.
- Stack must not become empty while `NavDisplay` is active.

## Host setup

- `NotDjinniNavDisplay` must keep these decorators:
  - `rememberSaveableStateHolderNavEntryDecorator()`
  - `rememberViewModelStoreNavEntryDecorator()`
  - `rememberResultEventBusNavEntryDecorator()`
- Keep fade transition specs unless task explicitly changes transitions.

## Results API

- Use official Navigation3 result bus only:
  - producer: `LocalResultEventBus.current.sendResult(...)`
  - consumer: `ResultEffect<T>(key)`.
- Custom result primitives and controller-level result APIs are forbidden.
