package not.djinni.presentation.screens.employer.vacancy.create

import not.djinni.model.seeker.vacancy.EmploymentType
import not.djinni.model.seeker.vacancy.JobCategoryCode

internal sealed interface CreateVacancyAction {
    data object ShowEmploymentTypeSheet : CreateVacancyAction
    data object ShowCategorySheet : CreateVacancyAction
    data object HideAlert : CreateVacancyAction
    data class SelectEmploymentType(val type: EmploymentType) : CreateVacancyAction
    data class SelectCategory(val category: JobCategoryCode) : CreateVacancyAction
    data class SubmitVacancy(
        val title: String,
        val description: String,
        val salaryMin: String,
        val salaryMax: String,
        val experienceYears: String,
    ) : CreateVacancyAction
}
