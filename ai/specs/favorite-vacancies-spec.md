# Favorite Vacancies

## Overview
Add a new favorite vacancies feature.

Users should be able to mark a vacancy as favorite or remove it from favorites from both the vacancies list and the vacancy details screen.

## User Goal
- Like or unlike a vacancy from the vacancies list.
- Like or unlike a vacancy from the vacancy details screen.
- Open the list of favorite vacancies from a top bar button.

## Requested Behavior
- Add a favorite-state icon to every vacancy card in the vacancies list.
- The vacancy card must reflect whether the vacancy is favorite.
- Users can mark a vacancy as favorite from the vacancies list.
- Users can remove a vacancy from favorites from the vacancies list.
- Add the same favorite/unfavorite behavior to the vacancy details screen.
- On the vacancy details screen, place the favorite icon in the top-right area of the top bar.
- In the vacancies list top bar, add a favorite button near the button that opens applied applications / applied vacancies.
- Tapping the favorite button in the top bar opens the Favorite Vacancies screen.

## UI Notes
- Use any temporary icons for the favorite action.
- Use two different icons or icon states so the user can distinguish favorite and non-favorite vacancies.
- The temporary icons may be replaced later.
- The favorite state must have two visual states:
  - filled
  - unfilled

## Favorite Vacancies Screen
- Use the screen name `Favorite Vacancies`.
- The screen shows the user's favorite vacancies.

## Backend Contract
Backend contract is provided in:

`/Users/hlibmokryi/Development/JVM/Kotlin/NotDjinni/NotDjinni/contracts/favorite-vacancies.md`

## Acceptance Criteria
- Each vacancy card in the vacancies list has a favorite-state icon.
- Users can add a vacancy to favorites from the vacancies list.
- Users can remove a vacancy from favorites from the vacancies list.
- Vacancy cards reflect the current favorite state.
- The vacancy details screen has a favorite-state icon in the top bar.
- Users can add a vacancy to favorites from the vacancy details screen.
- Users can remove a vacancy from favorites from the vacancy details screen.
- The top bar has a button that opens the Favorite Vacancies screen near the applied applications / applied vacancies button.
- Tapping the favorite button in the top bar opens the Favorite Vacancies screen.
