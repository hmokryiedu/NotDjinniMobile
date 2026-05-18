# Vacancy Profile Flow Fixes Report

## What Was Done

- Added employer vacancy duplication flow:
  - navigation route now supports `sourceVacancyId`
  - vacancy details now expose a single duplicate action/icon
  - create vacancy flow can prefill fields from an existing vacancy by loading source vacancy data
- Added profile editing support across API, request, repository, and mapper layers:
  - employer profile update request support
  - seeker profile update request support
  - work experience update mapping with `isCurrent` and nullable `endDate`
  - separate seeker edit profile screen
  - employer role editing flow with project-styled dark modal
- Fixed snackbar send-action path:
  - removed direct `viewModel.showSnackBar` usage from UI-facing flows
  - moved snackbar triggering through actions/view model handling
- Polished vacancy application UI:
  - apply bottom sheet no longer repeats vacancy title
  - `Cover Letter Templates` entry is visually emphasized and separated
  - template dialog updated to match dark project styling and revised actions/layout
- Added small QA/accessibility polish:
  - seeker main top icons now have content descriptions
  - seeker edit profile content aligned to match create profile styling
  - work experience validation UX improved for missing dates and current-position handling

## What Files Changed

- Employer vacancy duplication:
  - `app/src/main/kotlin/not/djinni/presentation/navigation/controller/EmployerEntry.kt`
  - `app/src/main/kotlin/not/djinni/presentation/navigation/controller/Screens.kt`
  - `app/src/main/kotlin/not/djinni/presentation/screens/employer/vacancy/details/*`
  - `app/src/main/kotlin/not/djinni/presentation/screens/employer/vacancy/create/*`
  - `app/src/main/kotlin/not/djinni/network/employer/*`
  - `app/src/main/kotlin/not/djinni/data/repository/DefaultEmployerRepository.kt`
  - `app/src/main/kotlin/not/djinni/domain/repository/EmployerRepository.kt`
- Employer and seeker profile editing:
  - `app/src/main/kotlin/not/djinni/network/employer/request/UpdateEmployerProfileRequest.kt`
  - `app/src/main/kotlin/not/djinni/network/seeker/request/UpdateSeekerProfileRequest.kt`
  - `app/src/main/kotlin/not/djinni/network/seeker/request/UpdateWorkExperienceRequest.kt`
  - `app/src/main/kotlin/not/djinni/network/seeker/*`
  - `app/src/main/kotlin/not/djinni/data/mapper/SeekerProfileMapper.kt`
  - `app/src/main/kotlin/not/djinni/data/repository/DefaultSeekerRepository.kt`
  - `app/src/main/kotlin/not/djinni/domain/repository/SeekerRepository.kt`
  - `app/src/main/kotlin/not/djinni/model/seeker/WorkExperience.kt`
  - `app/src/main/kotlin/not/djinni/presentation/navigation/controller/SeekerEntry.kt`
  - `app/src/main/kotlin/not/djinni/presentation/screens/seeker/profile/view/*`
  - `app/src/main/kotlin/not/djinni/presentation/screens/seeker/profile/edit/*`
  - `app/src/main/kotlin/not/djinni/presentation/screens/seeker/profile/create/CreateSeekerProfileViewModel.kt`
  - `app/src/main/kotlin/not/djinni/presentation/screens/employer/profile/my/*`
- SendAction and vacancy application UI polish:
  - `app/src/main/kotlin/not/djinni/presentation/screens/seeker/application/details/*`
  - `app/src/main/kotlin/not/djinni/presentation/screens/seeker/vacancy/details/*`
  - `app/src/main/kotlin/not/djinni/presentation/screens/seeker/vacancy/details/components/ApplyVacancyBottomSheet.kt`
  - `app/src/main/kotlin/not/djinni/presentation/screens/seeker/coverletter/templates/CoverLetterTemplatesScreen.kt`
  - `app/src/main/kotlin/not/djinni/presentation/screens/seeker/main/MainSeekerScreen.kt`
- Tests:
  - `app/src/test/kotlin/not/djinni/data/mapper/*`

## How To Test

### Unit Validation

- Run `./gradlew :app:testDebugUnitTest`
- Expected result: `BUILD SUCCESSFUL`

### Static Check

- Run `rg -n "viewModel\\.showSnackBar" app/src/main/kotlin -S`
- Expected result: no hits

### Emulator Validation Reference

- Date: May 18, 2026
- Device: Pixel 9 Pro AVD, `emulator-5554`
- Employer demo user: `demo-202605131244-02@notdjinni.test` / `User02Aa`
- Seeker demo user: `demo-202605131244-07@notdjinni.test` / `User07Aa`

### Scenarios

1. Employer vacancy duplication:
   - Open employer vacancy details
   - Verify only one duplicate icon is shown
   - Tap duplicate and confirm create vacancy screen opens prefilled from source vacancy
   - Edit fields, save, and verify a new vacancy is created
   - Press back and verify navigation returns to `My Vacancies`, not back into create flow
2. Employer profile edit:
   - Open employer profile
   - Change role and verify dark project-styled modal is shown
   - Verify modal has no `Cancel`
   - Save role only and verify company field remains read-only
3. Seeker profile flows:
   - Open seeker profile and verify profile view screen opens first
   - Open edit and verify separate screen layout matches create profile styling and left alignment
   - Add, update, and remove work experience locally, then save
   - Verify missing dates show validation message
   - Add valid current-position experience and verify profile renders `Present`
4. SendAction fix:
   - Verify no direct `viewModel.showSnackBar` usage remains
   - Withdraw application and confirm success feedback still works
   - Complete apply flow and confirm it succeeds
5. Vacancy application UI polish:
   - Open apply bottom sheet and verify vacancy title is not repeated
   - Verify `Cover Letter Templates` is emphasized and visually separated
   - Open templates dialog and verify:
     - no `Close`
     - no outside dismiss
     - delete action in top-right
     - `Edit` and `Apply` actions present
     - dark styling applied
     - no pink styling remains

### Passed Validation Summary

- `./gradlew :app:testDebugUnitTest` -> `BUILD SUCCESSFUL`
- `rg -n "viewModel\\.showSnackBar" app/src/main/kotlin -S` -> no hits
- Pixel 9 Pro emulator validation passed for:
  - employer vacancy duplication
  - employer profile edit
  - seeker profile flows
  - SendAction snackbar fix
  - vacancy application UI polish
