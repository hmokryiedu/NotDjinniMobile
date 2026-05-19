# Application Requirements Profile Actions Report

## What was done
- Unified vacancy apply availability logic through `EligibilityState?.isApplyAvailable()`, with `null` eligibility treated as not applicable for apply.
- Fixed the seeker vacancy details screen so `Apply Now` is not shown as active when seeker profile data or eligibility is missing.
- Updated the ViewModel apply gate to use the same shared helper as the UI.
- Extracted pure `calculateEligibility(...)` logic so only `min_experience_years` versus seeker `experienceYears` blocks apply.
- Kept salary mismatch as a warning only.
- Preserved existing already-applied behavior: `See Application` still has priority.

## What files changed
- `ai/report/application-requirements-profile-actions-report.md`
- Vacancy details eligibility logic and apply availability handling in seeker vacancy details UI/ViewModel files.
- Targeted unit test coverage for vacancy details eligibility behavior.

## How to test
1. Run:
   `./gradlew :app:testDebugUnitTest --tests "not.djinni.presentation.screens.seeker.vacancy.details.VacancyDetailsEligibilityTest"`
2. Confirm the task finishes with `BUILD SUCCESSFUL`.
3. Launch the debug build on Pixel 9 Pro AVD (`Pixel_9_Pro`).
4. Sign in as Sofia: `demo-202605131244-07@notdjinni.test`.
5. Open vacancy `Junior Android Eng Copyineer2` and confirm:
   - `1+ years experience`
   - `Your experience matches the requirements`
   - `Apply Now` is visible
6. Open vacancy `Viewed Jobs Backend Engineer Copy` and confirm:
   - `3+ years experience`
   - `Vacancy requires more experience than you have`
   - `You don't meet the experience requirements`
   - `Apply Now` is not shown
7. Sign in as Andrii: `demo-202605131244-06@notdjinni.test`.
8. Open vacancy `Junior Android Eng Copyineer2` and confirm:
   - salary warning `Salary is below your expectations ($5200)`
   - action `See Application`
   - salary warning does not block the already-applied state
