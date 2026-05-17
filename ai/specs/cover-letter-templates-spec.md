# Cover Letter Templates

## Overview

Add cover letter templates for seekers during vacancy application.

When a seeker applies to a vacancy, they can enter a cover letter in the existing cover letter sheet. This sheet must also provide a `Cover Letter Templates` action. Tapping it opens a screen where the seeker can view, create, edit, delete, and apply saved cover letter templates.

## User Goal

Seekers can reuse saved cover letter text while applying to vacancies, then adjust the inserted text before submitting the application if needed.

## Requested Behavior

- Add a `Cover Letter Templates` action to the cover letter sheet used during vacancy application.
- The action can be underlined or otherwise styled to look interactive.
- Tapping `Cover Letter Templates` opens the `Cover Letter Templates` screen.
- Applying a template returns the seeker to the previous vacancy application screen and inserts the selected template message into the cover letter text field.
- After applying a template, the seeker can still edit the inserted cover letter text before submission.

## Cover Letter Templates Screen

- Show a list of all cover letter templates.
- Each template item shows a preview of the template message.
- The preview should be approximately two or three lines.
- The screen has a plus action in the top bar.
- Tapping the plus action opens the same template dialog used for viewing/editing templates, but with an empty message.

## Template Dialog

When an existing template is tapped:

- Show a dialog on the `Cover Letter Templates` screen.
- Show the full template message.
- Provide an `Edit` action.
- Provide an `Apply` action.
- Provide a way to delete the cover letter template.

When `Edit` is tapped:

- Replace the static template text in the same dialog with a text field.
- The text field must not introduce visual differences from the regular text state.
- The edit state should feel smooth: use a basic text field without borders or extra visual chrome.
- Enter edit mode in the same dialog.
- Let the seeker edit the template message.
- Show a `Save` action.
- Saving updates the existing template.

When `Apply` is tapped:

- Apply the selected template to the cover letter field on the previous vacancy application screen.
- Return to the vacancy application flow with the template message inserted.

When creating a new template from the plus action:

- Open the same template dialog with an empty message.
- Let the seeker enter template text.
- Require a minimum message length of 10 characters.
- Keep the save action disabled until the minimum length is reached.
- Saving creates the new cover letter template.

## Backend Contract

Base: `/seeker/templates`

Auth: JWT required, `Authorization: Bearer <access_token>`

### Create

`POST /seeker/templates`

Request:

```json
{
  "message": "string"
}
```

Response `201 Created`:

```json
{
  "id": 1,
  "message": "string"
}
```

### List

`GET /seeker/templates`

Response `200 OK`:

```json
{
  "templates": [
    {
      "id": 1,
      "message": "string"
    }
  ]
}
```

No pagination, filter, or sort in current contract.

### Get One

`GET /seeker/templates/{id}`

Response `200 OK`:

```json
{
  "id": 1,
  "message": "string"
}
```

### Update

`PUT /seeker/templates/{id}`

Request:

```json
{
  "message": "string"
}
```

Response `200 OK`:

```json
{
  "message": "Template updated successfully"
}
```

### Delete

`DELETE /seeker/templates/{id}`

Response `200 OK`:

```json
{
  "message": "Template deleted successfully"
}
```

### Errors

- `401 Unauthorized`: invalid or missing token, plain string `"Token is not valid or has expired"`.
- `404 Not Found`: `{"message":"Seeker profile does not exist"}`.
- `404 Not Found`: `{"message":"Template not found"}`.
- `400 Bad Request`: `{"message":"Template message must not be blank"}`.

## Acceptance Criteria

- The vacancy application cover letter sheet shows a visible `Cover Letter Templates` action.
- Tapping `Cover Letter Templates` opens the `Cover Letter Templates` screen.
- The templates screen lists all seeker templates from `GET /seeker/templates`.
- Template list items show a short two- or three-line preview.
- Tapping a template opens a dialog with the full template message.
- The dialog supports applying the template to the vacancy application cover letter field.
- The dialog supports switching an existing template into edit mode.
- Edited templates can be saved through `PUT /seeker/templates/{id}`.
- New templates can be created from the top-bar plus action through `POST /seeker/templates`.
- New template save action is disabled until the message has at least 10 characters.
- Templates can be deleted through `DELETE /seeker/templates/{id}`.
- Applied template text remains editable in the vacancy application cover letter field.

## Open Questions

- Exact visual styling for the `Cover Letter Templates` action is not specified beyond making it look interactive.
- Empty, loading, and error states for the templates screen are not specified.
- Confirmation behavior before deleting a template is not specified.
