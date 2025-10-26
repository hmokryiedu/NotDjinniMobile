package not.djinni.presentation.screens.onboarding

import not.djinni.model.EmploymentType
import not.djinni.model.JobCategory
import not.djinni.model.Location

sealed interface OnboardingAction {
    data class PersonalInformationChanged(
        val fullName: String,
        val aboutMe: String
    ) : OnboardingAction

    data class WorkPreferencesChanged(
        val jobCategory: String,
        val workLocation: String,
        val employmentType: String,
        val yearsOfExperience: String,
        val salaryExpectations: String
    ) : OnboardingAction

    data object ShowJobCategoryAlert : OnboardingAction
    data object ShowEmploymentTypeAlert : OnboardingAction
    data object ShowLocationAlert : OnboardingAction
    data object DismissAlert : OnboardingAction

    data class SelectJobCategory(val jobCategory: JobCategory) : OnboardingAction
    data class SelectEmploymentType(val employmentType: EmploymentType) : OnboardingAction
    data class SelectLocation(val location: Location) : OnboardingAction

    data object Continue : OnboardingAction
}
