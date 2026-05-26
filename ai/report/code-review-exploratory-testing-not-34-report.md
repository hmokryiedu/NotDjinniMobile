# Code Review та Exploratory Testing — Agent Findings
## Summary
- Date: 2026-05-21
- Tester: Codex
- Build/backend target: Android emulator `emulator-5554`, AVD `Pixel_10_Pro`, boot completed; package `not.djinni` v`0.0.2` (last update `2026-05-19 13:20:42`); backend host hard-coded in `BaseClientBuilder.kt` to `192.168.0.81`
- Scope: Static code review and Pixel 10 Pro exploratory validation for seeker/employer vacancy, application, template, favorites, profile, auth-boundary, and logout flows across the requested report cases
- Evidence baseline: second validation pass used proof files under `/private/tmp/notdjinni-validation-2026-05-21/`, including `emu-avd-name.txt`, `emu-boot-completed.txt`, `emu-fingerprint.txt`, `package-path.txt`, and `package-version.txt`
- Overall result: More complete than the first pass, but not release-ready. Pixel 10 Pro runtime validation covers both seeker and employer paths historically, and latest correct-setup NOT-40 retry ends in FAIL because required seeker-side application path could not be completed and observed employer vacancy list/detail UI did not expose a response count label in the zero-state baseline. High-severity product gaps remain in NOT-36, NOT-38, NOT-40, and NOT-41, and NOT-46 still fails Jira expectation because logout MUST have a confirmation dialog with a cancel path and none is shown. Earlier build/unit-test compile blocker still stands.

## Code Review Findings
| Area | Finding | Severity | Evidence | Recommendation |
| --- | --- | --- | --- | --- |
| Applied vacancies navigation | Applied flow opens vacancy detail by `vacancyId` instead of application detail by `applicationId` | High | `app/src/main/kotlin/not/djinni/presentation/screens/seeker/vacancy/applied/AppliedVacanciesViewModel.kt:34` emits `NavigateToVacancyDetails(action.vacancyId)`; `AppliedVacanciesAction.kt:6` exposes only `OpenVacancy(vacancyId)`; `SeekerEntry.kt:38` routes click to vacancy details | Rewire applied list to an application model and open application detail by `applicationId` |
| Seeker applications filtering | No status filter exists in seeker applications | High | `ViewApplicationsAction.kt:3` only has load/open actions; `ViewApplicationsState.kt:8` has no filter state; `ViewApplicationsViewModel.kt:31` loads the full list only | Add status filter UI, state, and filtered query support |
| Withdraw application UX | Withdraw executes immediately with no confirmation/cancel path | High | `ApplicationDetailsScreen.kt:195` triggers withdraw directly; `ApplicationDetailsViewModel.kt:61` executes it immediately | Add a confirmation dialog before withdraw |
| Logout UX | Logout has no confirmation | Medium | `SeekerProfileScreen.kt:125`, `SeekerProfileViewModel.kt:39`, `EmployerProfileScreen.kt:126`, `EmployerProfileViewModel.kt:60` | Add logout confirmation dialog |
| Employer vacancy response visibility | Employer UI does not show applications/respond count | Medium | `VacancyCardData.kt:3` has no count; `VacancyCard.kt:25` renders no count; employer `VacancyDetailsScreen.kt:187` shows only a generic button | Add `applicationsCount` to card/detail models and render it in employer UI |
| Seeker application pagination | `limit` and `offset` are accepted but ignored in seeker application data source | Medium | `ApplicationDataSource.kt:19` takes `limit`/`offset`; `DefaultApplicationDataSource.kt:56` drops them | Pass pagination parameters through to the backend request |
| Backend environment config | Backend host is hard-coded to `192.168.0.81` | Medium | `BaseClientBuilder.kt:49` | Move backend host to environment/build config |
| Mutation failure feedback | Favorite/apply mutation failures have weak or missing user feedback | Low | `MainSeekerViewModel.kt:103` no failure message; `VacancyDetailsViewModel.kt:125` no `onFailure`; `FavoriteVacanciesViewModel.kt:67` no failure UX | Surface clear success/failure feedback for mutation actions |

## Exploratory Testing Results
| Jira Test | Scenario | Result | Key Evidence | Defect / Blocker |
| --- | --- | --- | --- | --- |
| NOT-35 | Guest opens vacancies feed and hits auth boundary on protected action | PASS | Screenshot: `/private/tmp/notdjinni-not35-auth-boundary.png` | None |
| NOT-36 | Seeker checks applied/applications flow | FAIL | Prior screenshot `/private/tmp/notdjinni-applications.png`; second pass account coverage with `seeker.001@notdjinni.local`, `seeker.057@notdjinni.local`, `seeker.038@notdjinni.local` | Missing status filter; applied item routing remains wrong. Empty state itself can be valid when no applications exist |
| NOT-37 | Seeker applies to vacancy using saved template | PASS | `/private/tmp/notdjinni-validation-2026-05-21/seeker038-product-designer-detail.xml`, `seeker038-templates-screen.xml`, `template-saved-success.xml`, `template-applied-to-application.xml`, `application-submit-result.xml` | None |
| NOT-38 | Seeker manages existing cover letter template | PARTIAL | `/private/tmp/notdjinni-validation-2026-05-21/seeker038-templates-direct.xml`, `template-reuse-attempt.xml`, `seeker038-template-edit-modal.xml`, `seeker038-template-edit-try3.xml`, `seeker038-template-edit-saved.xml` | Edit modal opens and existing template is visible, but delete affordance is missing and edit save did not complete/persist in observed attempts |
| NOT-39 | Employer duplicates own vacancy | PASS | `/private/tmp/notdjinni-validation-2026-05-21/employer097-vacancy-top-action.xml`, `employer097-duplicate-created.xml` | None |
| NOT-40 | Employer sees applications/respond count | FAIL | `/private/tmp/not40-seeker-count/device-proof.txt`, `/private/tmp/not40-seeker-count/package-proof.txt`, `03-employer-my-vacancies-baseline.png`, `05-employer-vacancy-detail-baseline-loaded.png`, `06-employer-applications-baseline.png`, `14-seeker-profile-before-edit.png`, `17-seeker-profile-after-edit.png`, `23-seeker024-search-epam.png`, `24-seeker024-search-react.png`; optional supporting zero-baseline evidence: `/private/tmp/not40-other-users/07-employer001-post-continue.xml`, `/private/tmp/not40-other-users/08-employer001-detail.xml`, `/private/tmp/not40-other-users/09-employer001-applications.xml` | Employer application count flow cannot be validated successfully: `employer.001@notdjinni.local` target vacancy is reachable in `My Vacancies`, but tested seeker accounts cannot discover/apply to same vacancy; employer list/detail also did not expose a count label in observed zero-state baseline. Expected: a seeker can apply to employer vacancy, and employer vacancy list/detail show matching response count. |
| NOT-41 | Seeker withdraws application and employer sees withdrawn state | FAIL | `/private/tmp/notdjinni-validation-2026-05-21/seeker038-real-application-detail-before-withdraw.xml`, `seeker038-withdraw-confirm-check.xml`, `employer097-product-designer-applications.xml`, `employer097-product-designer-apps-after-withdraw.xml` | Bug A: Withdraw action has no confirmation dialog or cancel step; tapping withdraw immediately changes status. Bug B: After seeker withdraws, employer vacancy applications list hides the withdrawn application and shows `No applications yet`; expected withdrawn application remains visible with `Withdrawn` status. |
| NOT-42 | Favorite/unfavorite vacancy flow | PASS | `POST /favorite/vacancy/731 -> 201`; item appeared in favorites; `DELETE /favorite/vacancy/731 -> 200`; favorites empty; screenshot: `/private/tmp/notdjinni-seeker-home.png` | Low feedback risk remains |
| NOT-44 | Profile edit persistence | PASS | `/private/tmp/notdjinni-validation-2026-05-21/employer097-profile-2.xml`, `employer097-change-role-screen.xml`, `employer097-profile-after-role-save.xml`, `employer097-profile-persistence.xml` | Employer `role` changed from `Talent Acquisition Specialist` to `HR` and persisted after logout/relogin. Seeker profile edit UI had been observed earlier; second-pass persistence proof is employer role only |
| NOT-45 | Employer profile edit preserves company field | PASS | `/private/tmp/notdjinni-validation-2026-05-21/employer097-profile-2.xml`, `employer097-change-role-screen.xml`, `employer097-profile-persistence.xml` | Role-only edit succeeded; company `FRACTAL` stayed visible, read-only, and unchanged |
| NOT-46 | Change role and logout behavior | FAIL | Profile persistence evidence above plus prior logout result screenshot `/private/tmp/notdjinni-logout-result.png` | Restart after logout passed, but logout MUST have a confirmation dialog with a cancel path, and none is shown |

## Defects
### Defect 1
- Summary: Debug/unit-test verification is blocked because public navigation functions expose internal `NavigationController`
- Environment: Local build verification on project workspace, command `./gradlew :app:testDebugUnitTest --console=plain`
- Preconditions: Project checked out with current source state
- Steps:
  1. Run `./gradlew :app:testDebugUnitTest --console=plain`.
- Actual result: Build fails before tests run with visibility errors in `NotDjinniNavDisplay.kt:26`, `AuthEntry.kt:9`, `EmployerEntry.kt:15`, `PublicEntry.kt:9`, `SeekerEntry.kt:18`, `SplashEntry.kt:8`: public function exposes internal parameter type `NavigationController`.
- Expected result: Project compiles and unit tests start executing.
- Severity: High
- Evidence: Failed Gradle test command; compile errors at the files/lines listed above
- Related test case: All validation/build verification

### Defect 2
- Summary: Seeker applied vacancies flow lacks status filter and routes applied items to vacancy detail instead of application detail
- Environment: Android emulator `emulator-5554`, AVD `Pixel_10_Pro`, app `not.djinni` v`0.0.2`
- Preconditions: Sign in as seeker and open applications/applied vacancies flow
- Steps:
  1. Open the seeker applications or applied vacancies screen.
  2. Inspect available controls for filtering by application status.
  3. Open an applied item from the list.
- Actual result: No status filter is available, and applied item routing is wired to vacancy detail by `vacancyId` rather than application detail by `applicationId`.
- Expected result: User can filter applications by status and open the selected application details.
- Severity: High
- Evidence: `ViewApplicationsAction.kt:3`, `ViewApplicationsState.kt:8`, `ViewApplicationsViewModel.kt:31`, `app/src/main/kotlin/not/djinni/presentation/screens/seeker/vacancy/applied/AppliedVacanciesViewModel.kt:34`, `AppliedVacanciesAction.kt:6`, `SeekerEntry.kt:38`
- Related test case: NOT-36

### Defect 3
- Summary: Employer application count flow cannot be completed and vacancy count is not visible in observed employer UI
- Environment: Android emulator `emulator-5554`, AVD `Pixel_10_Pro`, app `not.djinni` v`0.0.2`
- Preconditions: `employer.001@notdjinni.local` has `EPAM Systems` / `Frontend React Developer (Middle)`; tested seekers are `seeker.023@notdjinni.local` and `seeker.024@notdjinni.local`
- Steps:
  1. Sign in as `employer.001@notdjinni.local`, open `My Vacancies`, identify `EPAM Systems` / `Frontend React Developer (Middle)`, and capture applications baseline.
  2. Sign in as seeker and search for target vacancy by `EPAM`, `Frontend React Developer`, and `React`.
  3. Optionally edit `seeker.023@notdjinni.local` experience from `2 years` to `3 years`, then retry search/apply flow.
  4. Return to employer-side count check.
- Actual result: Employer vacancy is reachable and applications baseline is `No applications yet`, but tested seekers cannot discover/apply to target vacancy by `EPAM` / `Frontend React Developer` / `React` even after profile edit. Employer vacancy list/detail count behavior therefore cannot be proven through required seeker-created application path, and observed employer list/detail UI does not show a count label in the zero-state baseline.
- Expected result: Seeker can discover and apply to employer vacancy, and employer vacancy list/detail show response count matching applications.
- Severity: High
- Evidence: `/private/tmp/not40-seeker-count/device-proof.txt`, `/private/tmp/not40-seeker-count/package-proof.txt`, `03-employer-my-vacancies-baseline.png`, `05-employer-vacancy-detail-baseline-loaded.png`, `06-employer-applications-baseline.png`, `14-seeker-profile-before-edit.png`, `17-seeker-profile-after-edit.png`, `23-seeker024-search-epam.png`, `24-seeker024-search-react.png`; optional supporting zero-baseline evidence: `/private/tmp/not40-other-users/07-employer001-post-continue.xml`, `/private/tmp/not40-other-users/08-employer001-detail.xml`, `/private/tmp/not40-other-users/09-employer001-applications.xml`
- Related test case: NOT-40

### Defect 4
- Summary: Withdraw action has no confirmation dialog or cancel step; tapping withdraw immediately changes status
- Environment: Android emulator `emulator-5554`, AVD `Pixel_10_Pro`, app `not.djinni` v`0.0.2`
- Preconditions: Sign in as seeker and open an existing application detail
- Steps:
  1. Open application details.
  2. Trigger Withdraw.
- Actual result: Withdraw executes immediately with no confirmation dialog or cancel step, and tapping withdraw immediately changes status.
- Expected result: App should request confirmation before irreversible withdrawal.
- Severity: High
- Evidence: `/private/tmp/notdjinni-validation-2026-05-21/seeker038-withdraw-confirm-check.xml`; `ApplicationDetailsScreen.kt:195`, `ApplicationDetailsViewModel.kt:61`
- Related test case: NOT-41

### Defect 5
- Summary: After seeker withdraws, employer vacancy applications list hides the withdrawn application and shows `No applications yet`
- Environment: Android emulator `emulator-5554`, AVD `Pixel_10_Pro`, app `not.djinni` v`0.0.2`
- Preconditions: Existing employer vacancy has an application that the seeker withdraws
- Steps:
  1. Withdraw the application as seeker.
  2. Sign in as employer and open the vacancy applications list.
- Actual result: Employer later sees `No applications yet`; the withdrawn application disappears from the vacancy applications list.
- Expected result: Withdrawn application remains visible in the employer vacancy applications list with `Withdrawn` status.
- Severity: High
- Evidence: `/private/tmp/notdjinni-validation-2026-05-21/employer097-product-designer-applications.xml`, `employer097-product-designer-apps-after-withdraw.xml`
- Related test case: NOT-41

### Defect 6
- Summary: Cover letter template management is incomplete because delete is missing and edit save did not persist
- Environment: Android emulator `emulator-5554`, AVD `Pixel_10_Pro`, app `not.djinni` v`0.0.2`
- Preconditions: Sign in as seeker with at least one saved template
- Steps:
  1. Open templates list.
  2. Open an existing template for edit.
  3. Look for delete affordance.
  4. Modify content and save.
- Actual result: Existing template is visible and edit modal opens, but no delete affordance is present and edit save did not complete/persist in observed attempts.
- Expected result: User can delete an existing template and successfully save edited template content.
- Severity: High
- Evidence: `/private/tmp/notdjinni-validation-2026-05-21/seeker038-templates-direct.xml`, `template-reuse-attempt.xml`, `seeker038-template-edit-modal.xml`, `seeker038-template-edit-try3.xml`, `seeker038-template-edit-saved.xml`
- Related test case: NOT-38

### Defect 7
- Summary: Logout executes immediately without a required confirmation dialog
- Environment: Android emulator `emulator-5554`, AVD `Pixel_10_Pro`, app `not.djinni` v`0.0.2`
- Preconditions: User is signed in and opens seeker or employer profile
- Steps:
  1. Open profile screen.
  2. Tap Logout.
- Actual result: App logs the user out immediately and returns to sign-in.
- Expected result: App MUST ask for confirmation before logout and allow cancel.
- Severity: Medium
- Evidence: `SeekerProfileScreen.kt:125`, `SeekerProfileViewModel.kt:39`, `EmployerProfileScreen.kt:126`, `EmployerProfileViewModel.kt:60`, screenshot `/private/tmp/notdjinni-logout-result.png`
- Related test case: NOT-46

### Defect 8
- Summary: Favorite/apply mutations provide weak or no visible user feedback on success/failure
- Environment: Android emulator `emulator-5554`, AVD `Pixel_10_Pro`, app `not.djinni` v`0.0.2`
- Preconditions: Seeker can add/remove favorites or attempt apply/favorite mutations
- Steps:
  1. Favorite a vacancy from the list or details.
  2. Remove the vacancy from favorites.
  3. Observe in-app feedback for success or failure.
- Actual result: Backend calls succeed, but UI feedback is weak or absent; failure paths are not surfaced consistently.
- Expected result: User gets clear success/failure feedback for favorite/apply mutations.
- Severity: Low
- Evidence: `MainSeekerViewModel.kt:103`, `VacancyDetailsViewModel.kt:125`, `FavoriteVacanciesViewModel.kt:67`; runtime evidence `POST /favorite/vacancy/731 -> 201`, `DELETE /favorite/vacancy/731 -> 200`
- Related test case: NOT-42

## Risks
- Fresh build and unit-test verification are still blocked by a compile error, so regression confidence is limited even after the stronger second emulator pass.
- Release readiness is still blocked by functional failures in NOT-36, NOT-38, NOT-40, NOT-41, and NOT-46.
- Backend host remains hard-coded to `192.168.0.81`, so results stay environment-specific.
- Product-correct behaviors observed in validation should not be treated as defects: insufficient experience gating is valid, an empty applications state can be valid, and employer profile creation for an account without an employer profile is valid.

## Final Recommendation
Not ready for Confluence meeting notes or any release-readiness claim. Fix compile blocker first, then address confirmed functional failures in NOT-36, NOT-38, NOT-40, NOT-41, and NOT-46. For NOT-40, release scope remains unmet because seeker-to-employer application path could not be completed and observed employer vacancy UI did not show a response count label in baseline state. Rerun unit tests and focused Pixel 10 Pro emulator pass on same app line (`not.djinni` v`0.0.2`, installed build dated `2026-05-19 13:20:42`) with validated demo accounts after fixes.
