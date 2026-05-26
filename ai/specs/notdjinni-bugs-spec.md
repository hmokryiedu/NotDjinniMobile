# NotDjinni Bugs

## Overview
List of Jira Bug issues fetched from NotDjinni project `NOT`.

Source JQL:

```jql
project = "NOT" AND issuetype = Bug ORDER BY priority DESC, created DESC
```

Total bugs: 8.

## Bugs

### NOT-52: Cover letter template edit/delete management is incomplete

- Jira: https://glebmokryy.atlassian.net/browse/NOT-52
- Priority: High
- Status: TESTING
- Assignee: Gleb Mokryy
- Created: 2026-05-22T00:14:54.647+0300
- Updated: 2026-05-24T22:18:35.125+0300

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
Existing template is visible and edit modal opens, but delete affordance is missing. Edit save did not complete/persist in observed attempts.

#### Expected Result
User can edit and delete own saved template. Delete requires confirmation; failed delete/edit keeps stable local state and shows error feedback.

#### Impact
NOT-38 remains partial/failing for template management.

### NOT-51: Withdrawn applications disappear from employer vacancy applications list

- Jira: https://glebmokryy.atlassian.net/browse/NOT-51
- Priority: High
- Status: In Progress
- Assignee: Gleb Mokryy
- Created: 2026-05-22T00:14:52.307+0300
- Updated: 2026-05-24T21:55:31.203+0300

#### Environment
Android emulator `emulator-5554`, AVD `Pixel_10_Pro`, app `not.djinni` v`0.0.2`.

#### Preconditions
Existing employer vacancy has an application that the seeker withdraws.

#### Steps to Reproduce
1. Withdraw the application as seeker.
2. Sign in as employer.
3. Open the vacancy applications list.

#### Actual Result
Employer sees `No applications yet`; withdrawn application disappears from vacancy applications list.

#### Expected Result
Withdrawn application remains visible in employer vacancy applications list with `WITHDRAWN` / `Withdrawn` status.

#### Impact
NOT-41 fails. Employer loses visibility into withdrawn applications and application history.

### NOT-50: Withdraw application executes immediately without confirmation dialog

- Jira: https://glebmokryy.atlassian.net/browse/NOT-50
- Priority: High
- Status: TESTING
- Assignee: Gleb Mokryy
- Created: 2026-05-22T00:14:17.598+0300
- Updated: 2026-05-24T22:18:38.270+0300

#### Environment
Android emulator `emulator-5554`, AVD `Pixel_10_Pro`, app `not.djinni` v`0.0.2`.

#### Preconditions
User is signed in as seeker and opens existing application detail.

#### Steps to Reproduce
1. Open application details.
2. Tap `Withdraw application`.

#### Actual Result
Withdraw executes immediately with no confirmation dialog and no cancel step. Status changes immediately.

Code evidence:

- `ApplicationDetailsScreen.kt:195`
- `ApplicationDetailsViewModel.kt:61`

#### Expected Result
App shows confirmation dialog before withdraw and allows user to cancel.

#### Impact
NOT-41 fails. User can accidentally withdraw an application without confirmation.

### NOT-49: Employer vacancy application count is not visible and count flow cannot be validated

- Jira: https://glebmokryy.atlassian.net/browse/NOT-49
- Priority: High
- Status: In Progress
- Assignee: Gleb Mokryy
- Created: 2026-05-22T00:14:15.527+0300
- Updated: 2026-05-24T21:59:10.733+0300

#### Environment
Android emulator `emulator-5554`, AVD `Pixel_10_Pro`, app `not.djinni` v`0.0.2`.

#### Preconditions
`employer.001@notdjinni.local` has `EPAM Systems` / `Frontend React Developer (Middle)`. Tested seeker accounts include `seeker.023@notdjinni.local` and `seeker.024@notdjinni.local`.

#### Steps to Reproduce
1. Sign in as `employer.001@notdjinni.local`.
2. Open `My Vacancies`.
3. Identify `EPAM Systems` / `Frontend React Developer (Middle)`.
4. Capture applications baseline.
5. Sign in as seeker.
6. Search target vacancy by `EPAM`, `Frontend React Developer`, and `React`.
7. Try to apply.
8. Return to employer-side count check.

#### Actual Result
Employer vacancy is reachable and applications baseline is `No applications yet`, but tested seekers cannot discover/apply to target vacancy. Employer vacancy list/detail also did not show count label in zero-state baseline.

#### Expected Result
Seeker can discover and apply to employer vacancy. Employer vacancy list/detail show response count matching applications: `0 responses`, `1 response`, or `N responses`.

#### Impact
NOT-40 fails. Employer cannot verify application volume from list/detail UI.

### NOT-48: Seeker applied vacancies lack status filter and open wrong detail screen

- Jira: https://glebmokryy.atlassian.net/browse/NOT-48
- Priority: High
- Status: TESTING
- Assignee: Gleb Mokryy
- Created: 2026-05-22T00:14:13.247+0300
- Updated: 2026-05-24T22:18:38.723+0300

#### Environment
Android emulator `emulator-5554`, AVD `Pixel_10_Pro`, app `not.djinni` v`0.0.2`.

#### Preconditions
User is signed in as seeker and opens applications/applied vacancies flow.

#### Steps to Reproduce
1. Open seeker applications or applied vacancies screen.
2. Check available controls for filtering by application status.
3. Open an applied item from list.

#### Actual Result
No status filter is available. Applied item routing is wired to vacancy detail by `vacancyId` instead of application detail by `applicationId`.

#### Expected Result
User can filter applications by status and open selected application detail by `applicationId`.

#### Impact
NOT-36 fails. User cannot validate application-status workflow and may land on wrong screen.

### NOT-53: Logout executes immediately without required confirmation dialog

- Jira: https://glebmokryy.atlassian.net/browse/NOT-53
- Priority: Medium
- Status: TESTING
- Assignee: Gleb Mokryy
- Created: 2026-05-22T00:14:56.729+0300
- Updated: 2026-05-24T22:18:36.506+0300

#### Environment
Android emulator `emulator-5554`, AVD `Pixel_10_Pro`, app `not.djinni` v`0.0.2`.

#### Preconditions
User is signed in and opens seeker or employer profile.

#### Steps to Reproduce
1. Open profile screen.
2. Tap Logout.

#### Actual Result
App logs the user out immediately and returns to sign-in. No confirmation dialog or cancel path appears.

#### Expected Result
App asks for confirmation before logout and allows cancel.

#### Impact
NOT-46 fails Jira expectation. User can accidentally terminate session.

### NOT-33: Android: Vacancy does not switch to already applied state after application submission

- Jira: https://glebmokryy.atlassian.net/browse/NOT-33
- Priority: Medium
- Status: Done
- Assignee: Gleb Mokryy
- Labels: `android`
- Created: 2026-05-13T15:20:59.441+0300
- Updated: 2026-05-23T12:55:37.662+0300

#### Actual Result
After seeker submits an application for a vacancy, Android does not refresh/update the vacancy UI to show the user has already applied.

Vacancy remains in non-applied state after submission.

#### Expected Result
Submitted vacancy moves to an already-applied state immediately or after refresh. Apply action is no longer shown as available.

#### Impact
Seeker can see stale vacancy/application state after applying.

#### Acceptance Criteria
- After successful application submission, vacancy detail/list state reflects already-applied status.
- Apply action is hidden, disabled, or replaced with applied-state UI according to existing Android pattern.
- Returning to vacancy list/detail after submission does not show stale apply state.
- Failed submission does not mark vacancy as applied.

### NOT-54: Favorite and apply mutations provide weak or missing user feedback

- Jira: https://glebmokryy.atlassian.net/browse/NOT-54
- Priority: Low
- Status: TESTING
- Assignee: Gleb Mokryy
- Created: 2026-05-22T00:14:58.829+0300
- Updated: 2026-05-24T22:18:37.391+0300

#### Environment
Android emulator `emulator-5554`, AVD `Pixel_10_Pro`, app `not.djinni` v`0.0.2`.

#### Preconditions
User is signed in as seeker and can add/remove favorites or attempt apply/favorite mutations.

#### Steps to Reproduce
1. Favorite a vacancy from list or detail.
2. Remove the vacancy from favorites.
3. Observe in-app feedback for success/failure states.

#### Actual Result
Backend calls can succeed, but UI feedback is weak or absent. Failure paths are not surfaced consistently.

Code evidence:

- `MainSeekerViewModel.kt:103`
- `VacancyDetailsViewModel.kt:125`
- `FavoriteVacanciesViewModel.kt:67`

#### Expected Result
User sees clear success/failure feedback for favorite/apply mutations, and failure restores stable previous UI state where relevant.

#### Impact
NOT-42 passes main API flow, but UX/error confidence remains low.
