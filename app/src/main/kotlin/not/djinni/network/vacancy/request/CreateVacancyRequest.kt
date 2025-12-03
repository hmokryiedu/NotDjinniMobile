package not.djinni.network.vacancy.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import not.djinni.network.vacancy.response.EmploymentTypeResponse
import not.djinni.network.vacancy.response.JobCategoryCodeResponse
import not.djinni.network.vacancy.response.VacancyStatusCodeResponse

@Serializable
data class CreateVacancyRequest(
    @SerialName("title")
    val title: String,
    @SerialName("description")
    val description: String,
    @SerialName("salary_min")
    val salaryMin: Int,
    @SerialName("salary_max")
    val salaryMax: Int,
    @SerialName("min_experience_years")
    val minExperienceYears: Int,
    @SerialName("employment_type")
    val employmentType: EmploymentTypeResponse,
    @SerialName("category")
    val category: JobCategoryCodeResponse,
    @SerialName("status")
    val status: VacancyStatusCodeResponse
)
