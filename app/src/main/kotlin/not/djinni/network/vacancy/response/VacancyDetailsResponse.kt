@file:OptIn(ExperimentalTime::class)

package not.djinni.network.vacancy.response

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import not.djinni.network.employer.response.CompanyResponse
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
    data class VacancyDetailsResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("company")
    val company: CompanyResponse,
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("views_count")
    val viewsCount: Int,
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
    @SerialName("created_at")
    @Contextual
    val createdAt: Instant,
    @SerialName("updated_at")
    @Contextual
    val updatedAt: Instant
)
