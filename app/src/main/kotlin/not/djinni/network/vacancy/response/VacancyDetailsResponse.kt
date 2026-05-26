@file:OptIn(ExperimentalTime::class)

package not.djinni.network.vacancy.response

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import not.djinni.network.application.response.ApplicationStatusResponse
import not.djinni.network.employer.response.CompanyResponse
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
data class VacancyDetailsResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("application_id")
    val applicationId: Long? = null,
    @SerialName("application_status")
    val applicationStatus: ApplicationStatusResponse? = null,
    @SerialName("company")
    val company: CompanyResponse,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("views_count")
    val viewsCount: Int,
    @SerialName("applications_count")
    val applicationsCount: Int = 0,
    @SerialName("salary_min")
    val salaryMin: Int,
    @SerialName("salary_max")
    val salaryMax: Int,
    @SerialName("min_experience_years")
    val minExperienceYears: Int?,
    @SerialName("employment_type")
    val employmentType: EmploymentTypeResponse?,
    @SerialName("category")
    val category: JobCategoryCodeResponse?,
    @SerialName("status")
    val status: VacancyStatusCodeResponse,
    @SerialName("is_favorite")
    val isFavorite: Boolean = false,
    @SerialName("created_at")
    @Contextual
    val createdAt: Instant,
    @SerialName("updated_at")
    @Contextual
    val updatedAt: Instant
)
