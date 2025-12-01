package not.djinni.network.seeker.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateSeekerProfileRequest(
    @SerialName("speciality")
    val speciality: String,
    @SerialName("desired_salary")
    val desiredSalary: Int,
    @SerialName("experience_years")
    val experienceYears: Int,
    @SerialName("about_me")
    val aboutMe: String?,
    @SerialName("work_experience")
    val workExperience: List<CreateWorkExperienceRequest>
)