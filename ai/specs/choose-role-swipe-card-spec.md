# Choose Role Swipe Card Spec

## Goal

Replace the current two role option boxes on `ChooseRoleScreen` with one Tinder-like swipe card.

Verified target behavior:
- Idle card shows title content: icon, `Your role?`, `Swipe to choose`.
- Left drag previews/selects `Employer`.
- Right drag previews/selects `Seeker`.
- At 30% horizontal threshold, card content changes to the previewed role.
- At 80% horizontal threshold on release, card animates to the chosen side, role is selected, and app continues to the existing next-step navigation.
- No bottom `Continue` button.
- No extra status pill or role chip above/below the card.

## Current Code

Main file:
- `app/src/main/kotlin/not/djinni/presentation/screens/auth/role/ChooseRoleScreen.kt`

Current behavior:
- `Content` renders title `Choose your role`.
- `Role.entries` renders two `RoleItem` rows.
- User taps role, then taps `Continue`.
- `ChooseRoleAction.SelectRole(role)` stores selected role.
- `ChooseRoleAction.ProceedToMain` persists role and navigates through existing profile/main checks.

## Approved UI

Screen title:
- Use `What’s your role?`.
- Use project typography style close to `NotDjinniTheme.typography.title1Bold`.

Top role direction labels:
- Left side: `← Employer`.
- Right side: `Seeker →`.
- Labels stay text-only, not chips.
- Active drag direction brightens its label.

Card idle state:
- Single centered card.
- Uses project dark style:
  - screen background: `NotDjinniTheme.colors.background`
  - card background: `NotDjinniTheme.colors.highlightedContainer`
  - card/content text: `NotDjinniTheme.colors.onBackground` / `onSurface`
  - rounded shape from `NotDjinniTheme.shapes.great`
- Card content:
  - icon: `NotDjinniIcons.case`
  - title: `Your role?`
  - subtitle: `Swipe to choose`

Employer preview/selection:
- Direction: left drag.
- Accent: orange local UI color `Color(0xFFFF8A00)`.
- Icon: `NotDjinniIcons.case`.
- Title: `Employer`.
- Subtitle: `Post jobs & hire`.
- Orange glow/shadow intensity grows with swipe progress.

Seeker preview/selection:
- Direction: right drag.
- Accent: `NotDjinniTheme.colors.success`.
- Icon: `NotDjinniIcons.zoom`.
- Title: `Seeker`.
- Subtitle: `Find & apply`.
- Green glow/shadow intensity grows with swipe progress.

Motion:
- Card translates slightly with drag.
- Card rotates toward swipe direction.
- Card content rotates together with the card as one visual layer.
- No counter-rotation on icon, title, or subtitle.
- Under 80% release: card animates back to center.
- At or above 80% release: card animates outward toward chosen side, then triggers existing role selection/navigation.

Bottom area:
- Remove `Continue`.
- Remove bottom status card/pill.
- Keep only two tiny passive dots near the bottom, matching the approved reference. They have no background panel and no click behavior.

## Implementation Plan

1. Update `ChooseRoleScreen.kt`, `ChooseRoleAction.kt`, and `ChooseRoleViewModel.kt`.
2. Replace `RoleItem` list and `NotDjinniButton` with a new private `SwipeRoleCard` composable.
3. Keep `ChooseRoleState`.
4. Replace old actions with one action: `ChooseRoleAction.ConfirmRole(role)`, so auto-navigation does not depend on dispatching `SelectRole` and `ProceedToMain` separately.
5. In `ChooseRoleViewModel`, handle confirm by:
   - updating `selectedRole`
   - persisting the confirmed role
   - navigating through the same existing seeker/employer profile checks
6. Remove old `SelectRole` and `ProceedToMain` actions because preview state is local to the swipe card and there is no `Continue` button.
7. Use Compose gesture handling with horizontal drag state:
   - calculate `progress = abs(offsetX) / commitThresholdPx`, clamped to `0f..1f`
   - preview role when `abs(offsetX) >= previewThresholdPx`
   - commit role when `abs(offsetX) >= commitThresholdPx` on drag end
8. On commit, send only the confirmed role action.
9. Use no new production dependencies.
10. No `ai/rules/*` update required because this is a localized UI behavior change, not a core architecture change.

## Validation

Allowed validation:
- Unit tests.
- Emulator QA skill.

Checks:
- Idle state shows `What’s your role?`, `← Employer`, `Seeker →`, centered card, no bottom button.
- Short drag below 30% keeps idle card content.
- Drag past 30% left changes card content to Employer and lights orange.
- Drag past 30% right changes card content to Seeker and lights green.
- Release below 80% returns card to center and does not navigate.
- Release past 80% left selects Employer and navigates through existing employer path.
- Release past 80% right selects Seeker and navigates through existing seeker path.
- Card content tilts together with card.
