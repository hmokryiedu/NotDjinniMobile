# Seeker Vacancy Search Fix

## Overview

Fix seeker vacancy search so searching for visible vacancy terms returns matching vacancies instead of an empty list.

## User Goal

As a job seeker, I want vacancy search to return relevant results for text I can see in vacancy titles, companies, categories, or related searchable fields, so I can quickly find matching vacancies.

## Problem

During validation on `Pixel_9_Pro`, seeker vacancy search appeared broken.

Repro:
1. Login as seeker `demo-202605131244-07@notdjinni.test / User07Aa`.
2. Open seeker vacancies.
3. Enter `Android` in search.

Expected:
- Android vacancies remain visible, or matching vacancy results appear.

Actual:
- Vacancy list becomes empty despite visible Android-related titles such as `Junior Android Eng Copyineer2`.
- No explicit empty-state message is shown.

## Requested Behavior

- Search query `Android` must return matching seeker vacancies when Android-related vacancies exist.
- Search matching must be case-insensitive.
- Search must match at least vacancy title and any other fields already intended by the existing product behavior.
- Empty search results must show a clear empty state, such as `No vacancies found`.
- Clearing the search query must restore the unfiltered vacancy list.
- Search must not break vacancy details navigation or application state indicators.

## Fix Direction

- Inspect seeker vacancy search pipeline from text input to data source call.
- Confirm whether filtering is local, remote, or mixed.
- Align UI query handling with the actual filtering/search contract.
- Verify substring matching against supported searchable fields.
- Add explicit UI handling for zero-result search state.

## Acceptance Criteria

- Given a seeker is logged in and Android vacancies exist, when the seeker searches `Android`, matching vacancies remain visible.
- Given a seeker searches with different casing such as `android` or `ANDROID`, matching vacancies still appear.
- Given a seeker searches a term with no matches, the screen shows an explicit empty state.
- Given a seeker clears search text, the original vacancy list is restored.
- Given a seeker opens a search result, vacancy details still open correctly.
- Existing vacancy application state remains correct after search, including `Apply Now` and `See Application` states.

## Validation

- Run relevant unit tests for vacancy search/filter behavior.
- Validate manually on `Pixel_9_Pro` using seeker `demo-202605131244-07@notdjinni.test / User07Aa`.
- Re-run the original failing scenario with search query `Android`.

## Out Of Scope

- Do not redesign the vacancy list screen.
- Do not change employer vacancy creation behavior.
- Do not change authentication, role selection, or application creation flows.
