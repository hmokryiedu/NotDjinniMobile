# Vacancy Application Cover Letter Templates Report

## What Was Done

- Added `applications_count` support from the vacancy details network response through the vacancy domain/display model to the vacancy details UI.
- Fixed vacancy details refresh after a successful apply so the screen reloads updated application state and counts.
- Added a generic navigation result API used to return the selected cover-letter template back into the apply flow.
- Added cover-letter templates CRUD flow, including data/domain/repository/use case/UI support.
- Wired cover-letter template selection into the apply sheet so the selected template is inserted into the cover letter field and remains editable.
- Added withdraw application support across data/domain/use case/UI so an applied application can transition to withdrawn state from application details.

## Files Changed

- Vacancy details and apply flow:
  - vacancy details response/model/mapper/state/UI updates for `applications_count`
  - apply success refresh handling
  - apply sheet integration for selected cover-letter template
- Navigation/result handling:
  - generic navigation result API for returning selected values between screens
- Cover-letter templates:
  - network/data/repository/domain/use case layers
  - templates list/create/edit/delete UI flow
- Application withdraw flow:
  - application data/domain/use case changes
  - application details UI/state updates for withdraw and post-action refresh
- Tests:
  - unit coverage for the new vacancy/application/template behavior

## How To Test

### Unit Validation

- Run:
  - `./gradlew :app:testDebugUnitTest`
- Expected result:
  - `BUILD SUCCESSFUL`

### Pixel 9 Pro Emulator QA

- Device:
  - serial `emulator-5554`
  - AVD `Pixel_9_Pro`
- User:
  - seeker `demo-202605131244-07@notdjinni.test`

### Scenarios

1. Open vacancy details and verify the stat row shows counts. In validated run, before apply the counts were `2` and `1`.
2. Start apply flow and verify the apply sheet can open `Cover Letter Templates`.
3. In templates, verify CRUD:
   - create `QA_template_0517`
   - edit and confirm it persists as `QA_template_edit_0517517`
   - delete it and confirm the empty state returns `No templates yet`
4. Select a template from the templates flow and verify it is inserted into the apply sheet cover letter field. Edit the inserted text manually and confirm it remains editable.
5. Submit the application and verify navigation returns to vacancy details, the primary action changes from `Apply Now` to `See Application`, and `applications_count` updates. In validated run, the relevant count changed from `1` to `2`, while the stat row after submit showed `2` and `2`.
6. Open `See Application`, verify `Withdraw` is available for `Applied` status, perform withdraw, and verify the screen reloads with status `Withdrawn` and the `Withdraw` button disappears.

### Validation Notes

- Passed validation:
  - `./gradlew :app:testDebugUnitTest`
  - Pixel 9 Pro Emulator QA on `emulator-5554`
- Artifacts:
  - `/tmp/notdjinni-launch.png`
  - `/tmp/notdjinni-post-login.png`
  - `/tmp/notdjinni-vacancy-details.png`
  - `/tmp/notdjinni-vacancy-details.xml`
  - `/tmp/notdjinni-after-submit.xml`
  - `/tmp/notdjinni-application-details.xml`
  - `/tmp/notdjinni-after-withdraw.xml`
- Note:
  - the UI tree exposed raw stat numbers but not labels/icons, so the views/applications mapping was inferred from layout position and the count change after apply.
