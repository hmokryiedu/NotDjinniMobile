package not.djinni.presentation.screens.employer.vacancy.create

import androidx.compose.runtime.Immutable
import not.djinni.model.seeker.vacancy.EmploymentType
import not.djinni.model.seeker.vacancy.JobCategoryCode

@Immutable
internal data class CreateVacancyState(
    val selectedEmploymentType: EmploymentType? = null,
    val selectedCategory: JobCategoryCode? = null,
    val alert: CreateVacancyAlert? = null,
)
