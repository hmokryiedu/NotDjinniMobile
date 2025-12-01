package not.djinni.network.seeker.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import not.djinni.network.vacancy.response.JobCategoryCodeResponse

@Serializable
data class SeekerProfileResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("about_me")
    val aboutMe: String?,
    @SerialName("speciality")
    val speciality: String,
    @SerialName("desired_salary")
    val desiredSalary: Int,
    @SerialName("experience_years")
    val experienceYears: Int,
    @SerialName("job_category")
    val jobCategory: JobCategoryCodeResponse,
    @SerialName("work_experience")
    val workExperience: List<WorkExperienceResponse>
)