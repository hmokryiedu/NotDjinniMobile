# Application Actions and Filters

## Overview
В приложении не хватает нескольких UX-функций и исправлений, связанных с applied vacancies, application actions, cover letter templates, logout и feedback для favorite/apply mutations.

## Requested Behavior
- Добавить фильтрацию applied vacancies по статусу.
- Добавить popup со списком status filters.
- Разместить фильтр над списком applied vacancies.
- Показывать текущий выбранный фильтр на кнопке фильтра.
- Менять цвет кнопки фильтра в зависимости от выбранного статуса.
- Если статус не выбран, показывать текст вроде `Select status filter`.
- Добавить confirmation popup для `withdraw` application.
- Добавить confirmation popup для logout.

## Jira Bugs

### NOT-52: Cover letter template edit/delete management is incomplete
Source: https://glebmokryy.atlassian.net/browse/NOT-52

Status: `In Progress`  
Priority: `High`

#### Environment
Android emulator `emulator-5554`, AVD `Pixel_10_Pro`, app `not.djinni` v`0.0.2`.

#### Preconditions
User is signed in as seeker and has at least one saved cover letter template.

#### Steps to Reproduce
1. Open templates list.
2. Open existing template for edit.
3. Look for delete action.
4. Modify content and save.

#### Actual Result
- Existing template is visible.
- Edit modal opens.
- Delete affordance is missing.
- Edit save did not complete or persist in observed attempts.

#### Expected Result
- User can edit own saved template.
- User can delete own saved template.
- Delete requires confirmation.
- Failed delete or edit keeps stable local state and shows error feedback.

#### Impact
`NOT-38` remains partial or failing for template management.

### NOT-54: Favorite and apply mutations provide weak or missing user feedback
Source: https://glebmokryy.atlassian.net/browse/NOT-54

Status: `To Do`  
Priority: `Low`

#### Environment
Android emulator `emulator-5554`, AVD `Pixel_10_Pro`, app `not.djinni` v`0.0.2`.

#### Preconditions
User is signed in as seeker and can add or remove favorites or attempt apply/favorite mutations.

#### Steps to Reproduce
1. Favorite a vacancy from list or detail.
2. Remove the vacancy from favorites.
3. Observe in-app feedback for success and failure states.

#### Actual Result
- Backend calls can succeed, but UI feedback is weak or absent.
- Failure paths are not surfaced consistently.

#### Expected Result
- User sees clear success feedback for favorite/apply mutations.
- User sees clear failure feedback for favorite/apply mutations.
- Failure restores stable previous UI state where relevant.

#### Impact
`NOT-42` passes main API flow, but UX/error confidence remains low.

## UI Notes
- Applied vacancies status filter should be visible above the vacancies list.
- Status filter should open as a popup.
- Status filter button should show the selected status.
- Status filter button color should reflect the selected status.
- Empty status filter state should use text similar to `Select status filter`.
- Withdraw action should require user confirmation before execution.
- Logout should require user confirmation before execution.
- Cover letter template delete should require user confirmation.

## Acceptance Criteria
- Applied vacancies can be filtered by selected status.
- User can open a popup with available status filters.
- Current selected applied vacancies status filter is visible above the list.
- Filter button color changes according to selected status.
- When no status is selected, filter button shows empty-state text similar to `Select status filter`.
- Withdraw application action opens a confirmation popup before the action is performed.
- Logout opens a confirmation popup before logout is performed.
- Saved cover letter templates can be edited.
- Saved cover letter templates can be deleted.
- Cover letter template delete action opens a confirmation popup before deletion.
- Failed cover letter template edit/delete keeps stable local state and shows error feedback.
- Favorite/apply mutation success and failure states show clear user feedback.
- Failed favorite/apply mutation restores stable previous UI state where relevant.

## Open Questions
- `NOT-54` describes weak or missing feedback for favorite/apply mutations, while the supplied note also mentions missing logout confirmation. Confirm whether logout confirmation should be handled together with `NOT-54` or as a separate task.
