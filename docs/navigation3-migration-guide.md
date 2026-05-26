# Navigation 2 to Navigation 3 Migration Guide

This guide describes practical migration path from Navigation 2 to project Navigation 3 architecture.

Target architecture and final project pattern: [Navigation3 Integration Guide](./navigation3-integration-guide.md).

Primary sources used:
- `docs/navigation3-integration-guide.md`
- `/Users/hlibmokryi/.codex/skills/navigation-3/SKILL.md`

## Scope and prerequisites

Before starting migration:
- Compose destinations already used.
- Type-safe routes migration done first (replace string route usage in Navigation 2 flow).
- `compileSdk` is `36+`.
- `minSdk` is `23+`.

If prerequisites not met, stop migration and close gaps first.

## Dependencies

Replace Navigation 2 runtime surface with Navigation 3 libraries:
- required: `androidx.navigation3:navigation3-runtime`
- required: `androidx.navigation3:navigation3-ui`
- optional: `androidx.lifecycle:lifecycle-viewmodel-navigation3` (only when ViewModel-scoped `NavEntry` integration needed)

After full migration, remove Navigation 2 dependencies from module(s).

## Routes migration

Migrate route model:
- from: string routes and argument strings
- to: typed `@Serializable` models implementing `NavKey`

Rules:
- one route type per destination contract
- arguments become typed fields in `NavKey` model
- avoid mixed string + typed routing in same migrated flow

## Navigation state ownership

Replace `NavController` ownership model with project wrapper pattern:
- create/use project `NavigationController`
- own active back stack via `rememberNavBackStack`
- keep single owner per active stack
- keep stack non-empty while display mounted

Do not keep parallel ownership via old `NavController`.

## Destination registration

Replace Navigation 2 destination graph APIs:
- `NavHost` + `NavGraphBuilder` + `composable` + `dialog`
- with `entryProvider { ... }` + typed `entry<NavKeyType> { ... }`

Project pattern:
- split entries by feature
- compose feature entry providers in one top-level provider
- keep key type and `entry<...>` type matched 1:1

## Display layer

Replace host rendering:
- from `NavHost`
- to `NavDisplay`

Use project decorators stack from target architecture guide, including:
- state holder decorator
- ViewModel store decorator
- result event bus decorator

Do not invent alternative host pipeline during migration.

## Returning results

Use official Navigation 3 result bus only:
- `ResultEffect`
- `LocalResultEventBus`

Rule:
- no custom result store for migrated flows
- deterministic result keys, single producer/consumer contract per key

## ViewModel boundary

Boundary rule during migration:
- ViewModel emits side effects (`NavigateBack`, `OpenX`, etc.)
- UI layer observes side effects and calls `NavigationController`
- ViewModel does not mutate back stack directly
- ViewModel does not depend on controller implementation types

## Cleanup after migration

After migrated flow works on Navigation 3:
- remove Navigation 2 dependencies
- remove obsolete Navigation 2 imports/usages
- remove obsolete string-route helpers related to migrated flow

Keep cleanup scoped to migrated areas only.

## Caveats and extra-plan cases

Plan separately (recipe-level or extra migration plan), not inline ad-hoc:
- deep links
- bottom sheets
- modular navigation across modules
- multiple back stacks
- custom destination/scene types
- nested or shared destination ownership patterns

For these cases, follow relevant recipes and architecture guidance from navigation-3 skill references, then adapt to project target architecture.
