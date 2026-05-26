@file:OptIn(ExperimentalTime::class)

package not.djinni.model.seeker.vacancy

import not.djinni.model.company.Company
import not.djinni.model.application.ApplicationStatus
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class Vacancy(
    val id: Long,
    val applicationId: Long? = null,
    val applicationStatus: ApplicationStatus? = null,
    val company: Company,
    val title: String,
    val description: String,
    val viewsCount: Int,
    val applicationsCount: Int,
    val salaryMin: Int,
    val salaryMax: Int,
    val minExperienceYears: Int?,
    val employmentType: EmploymentType,
    val category: JobCategoryCode?,
    val status: VacancyStatusCode,
    val isFavorite: Boolean = false,
    val createdAt: Instant,
    val updatedAt: Instant
)
