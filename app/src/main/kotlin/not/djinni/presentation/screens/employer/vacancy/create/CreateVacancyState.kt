package not.djinni.presentation.screens.employer.vacancy.create

import androidx.compose.runtime.Immutable
import not.djinni.model.seeker.vacancy.EmploymentType
import not.djinni.model.seeker.vacancy.JobCategoryCode

@Immutable
internal data class CreateVacancyState(
    val title: String = "",
    val description: String = "",
    val salaryMin: String = "",
    val salaryMax: String = "",
    val experienceYears: String = "",
    val selectedEmploymentType: EmploymentType? = null,
    val selectedCategory: JobCategoryCode? = null,
    val prefillVersion: Int = 0,
    val alert: CreateVacancyAlert? = null,
)
