# Vacancy Application Updates

## Overview

Implement several fixes and new behaviors for vacancy details and application details.

## Requested Behavior

- On the vacancy details screen, show not only the views count, but also the applications count: how many people applied to the vacancy.
- Show the applications count next to the views count in the same `row`.
- Fix the bug after applying to a vacancy:
  - Seeker opens vacancy details.
  - Seeker taps `Apply now`.
  - Seeker applies to the vacancy.
  - Cover letter is sent.
  - Snackbar is shown.
  - Vacancy status must be updated.
- After successful `Apply now`, update state by fetching updated vacancy data, so the `Apply now` button is no longer shown for a vacancy the seeker has already applied to.
- Add the ability to withdraw an application from the application details screen.
- Add a bottom `Withdraw` button on the application details screen.
- Do not show the `Withdraw` button when application status is `WITHDRAWN`, `HIRED`, or `REJECTED`.
- After tapping `Withdraw`, call the backend withdraw endpoint.
- After successful withdraw, update application data so the application status changes in the UI.

## Backend Contract

### Withdraw Application

`PATCH /application/{id}/withdraw`

Auth: required JWT, `Authorization: Bearer <access_token>`.

Request body: none.

Success `200 OK`:

```json
{
  "message": "Application withdrawn successfully"
}
```

Behavior:

- Only authenticated seeker can withdraw own application.
- `APPLIED`, `REVIEWING`, `INTERVIEW`, `TEST_TASK`, `OFFER` -> set to `WITHDRAWN`.
- `WITHDRAWN` -> idempotent success, no DB update.
- `HIRED`, `REJECTED` -> reject.

Errors:

- `401 Unauthorized`: invalid/missing token, body is plain string: `"Token is not valid or has expired"`.
- `404 Not Found`: `{"message":"Job seeker profile not found"}`.
- `404 Not Found`: `{"message":"Application not found"}`.
- `403 Forbidden`: `{"message":"Unauthorized to access this application"}`.
- `400 Bad Request`: `{"message":"Cannot withdraw application with status HIRED"}` or `REJECTED`.

## Acceptance Criteria

- Vacancy details show both views count and applications count next to each other.
- After seeker successfully applies to a vacancy, vacancy details are refreshed from updated vacancy data.
- After successful apply, already-applied vacancy state is reflected in UI and `Apply now` is no longer shown.
- Application details screen has a bottom `Withdraw` button when application status is not `WITHDRAWN`, `HIRED`, or `REJECTED`.
- Application details screen does not show the `Withdraw` button for `WITHDRAWN`, `HIRED`, or `REJECTED` statuses.
- Tapping `Withdraw` calls `PATCH /application/{id}/withdraw`.
- After successful withdraw, application details are refreshed from updated application data.
- After successful withdraw, application status changes in UI.
