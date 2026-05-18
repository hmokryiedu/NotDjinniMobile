# UI Fixes

## Overview
Fix several UI issues in seeker-facing screens and the add work experience flow.

## Requested Behavior
- In `CoverLetterTemplatesScreen.kt`, hide the cover template popup before navigating back when the apply button is clicked.
- Fix vacancy cards so the company title cannot hide or overlap the like button.
- Add styling for the date picker on the work experience screen.
- Change the checkbox color on the add work experience screen.

## UI Notes
- Vacancy card company titles can be long and must not cover the like button.
- The provided screenshot shows a vacancy card where the company title is close to the like button area.

## Acceptance Criteria
- Applying a cover letter template hides the popup before back navigation starts.
- Vacancy card company title text no longer hides or overlaps the like button.
- Work experience date picker has the requested styling applied.
- Add work experience checkbox uses the updated color.

## Open Questions
- The exact date picker styling was not specified.
- The exact checkbox color was not specified.
