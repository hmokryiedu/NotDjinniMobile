@file:OptIn(ExperimentalTime::class)

package not.djinni.model.seeker.vacancy

import not.djinni.model.company.Company
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class Vacancy(
    val id: Long,
    val company: Company,
    val title: String,
    val description: String,
    val salaryMin: Int,
    val salaryMax: Int,
    val minExperienceYears: Int?,
    val employmentType: EmploymentType,
    val category: JobCategoryCode?,
    val status: VacancyStatusCode,
    val createdAt: Instant,
    val updatedAt: Instant
)
