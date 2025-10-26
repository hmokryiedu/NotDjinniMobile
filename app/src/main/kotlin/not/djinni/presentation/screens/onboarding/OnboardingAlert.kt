package not.djinni.presentation.screens.onboarding

import not.djinni.model.EmploymentType
import not.djinni.model.JobCategory
import not.djinni.model.Location

sealed interface OnboardingAlert {
    data class LocationSelection(
        val locations: List<Location>,
        val selected: Location?
    ) : OnboardingAlert

    data class EmploymentTypeSelection(
        val types: List<EmploymentType>,
        val selected: EmploymentType?
    ) : OnboardingAlert

    data class JobCategorySelection(
        val categories: List<JobCategory>,
        val selected: JobCategory?
    ) : OnboardingAlert
}
