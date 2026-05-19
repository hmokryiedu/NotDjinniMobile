# Whole Application Validation Report

## Scope

- Validation target: whole application
- Device: `Pixel_9_Pro` AVD
- Emulator: `emulator-5554`
- AVD confirmation: `adb emu avd name -> Pixel_9_Pro`
- App under test: `not.djinni`
- Version from source: `0.0.2 (versionCode 2)`

## What Was Done

- Ran `./gradlew :app:testDebugUnitTest --console=plain` and it passed.
- Ran `./gradlew :app:installDebug --console=plain --quiet` and install succeeded.
- Validated unauthenticated launch, authentication errors, seeker flow, employer flow, session persistence, and logout on `Pixel_9_Pro`.
- Confirmed backend reachability for auth, vacancy list, application creation, and vacancy creation.

## Validation Results

### Passed

- Fresh unauthenticated launch opens Sign In screen.
- Invalid email format is rejected on Sign In with inline error `Email is not valid`.
- Valid email + wrong password is rejected with `Invalid credentials`.
- Valid seeker login works.
- After login, role chooser appears and `Continue` is disabled until a role is selected.
- Seeker role opens seeker vacancies flow.
- Vacancy list loads on seeker side.
- Vacancy details open correctly.
- Ineligible vacancy shows experience gate and does not expose apply CTA.
- Eligible vacancy allows apply flow.
- Empty cover letter submission works; application is created.
- After apply, CTA changes to `See Application`.
- Application details screen opens and shows status/timeline.
- Applied vacancies list opens.
- Seeker profile opens.
- Logout works and returns to Sign In.
- Valid employer login works.
- Employer role opens employer vacancies flow.
- Employer vacancy creation form opens.
- Empty submit on create form shows `Please fill all required fields`.
- Valid vacancy creation works and opens created vacancy details.
- Employer `View Applications` opens empty state `No applications yet`.
- App restart with active session returns to role chooser, so session persistence is working.

### Finding 1: Search Appears Broken On Seeker Vacancy List

- Severity: Medium
- Status: Open

Repro:
1. Login as seeker `demo-202605131244-07@notdjinni.test / User07Aa`
2. Open Seeker vacancies
3. Enter `Android` in search

Expected:
- Visible Android vacancies remain, or filtered matches appear.

Actual:
- List becomes empty, despite visible titles like `Junior Android Eng Copyineer2`.
- No explicit empty-state message is shown.

Likely fix direction:
- Verify the query passed from UI to vacancy filtering or backend search.
- Verify case-insensitive substring matching against title, company, and category.
- Show explicit empty state such as `No vacancies found` when result set is empty.

## Observations

- Backend was reachable enough for auth, list, apply, and create flows.
- No user-facing crashes, ANRs, or forced app exits were seen during validation.
- Raw `adb logcat` extraction hit a host sandbox or daemon limitation late in the run, so crash assessment is based on on-device behavior and mobile-tool inspection rather than a saved full logcat dump.

## Coverage Gaps

- Numeric-field edge cases in vacancy creation were not fully validated beyond empty required-field validation.
- Search was not re-run after application state changes to isolate backend search versus local filtering.
- No instrumentation tests were present or run; only JVM unit tests were available.

## Files Changed

- `ai/report/whole-application-validation-report.md`

## How To Test

1. Run `./gradlew :app:testDebugUnitTest --console=plain`
2. Run `./gradlew :app:installDebug --console=plain --quiet`
3. Launch `not.djinni` on `Pixel_9_Pro` (`emulator-5554`)
4. Validate sign-in error cases:
   - invalid email shows `Email is not valid`
   - valid email + wrong password shows `Invalid credentials`
5. Login as seeker and verify:
   - role chooser blocks `Continue` until selection
   - vacancy list and vacancy details open
   - ineligible vacancy hides apply CTA
   - eligible vacancy allows apply
   - empty cover letter submit creates application
   - post-apply CTA changes to `See Application`
   - application details, applied vacancies, profile, and logout work
6. Login as employer and verify:
   - employer vacancies flow opens
   - create form empty submit shows `Please fill all required fields`
   - valid vacancy creation opens created vacancy details
   - `View Applications` shows `No applications yet` for empty state
7. Restart app with active session and verify role chooser opens again
8. Reproduce search defect with seeker user `demo-202605131244-07@notdjinni.test / User07Aa` and query `Android`

## How We Can Fix The Found Issue

- Inspect seeker vacancy search pipeline from text input to data source call.
- Confirm whether filtering is local, remote, or mixed, then align behavior with the expected matching fields.
- Add or update unit coverage around search matching and empty-result behavior.
- Add explicit UI empty state for zero search results.
