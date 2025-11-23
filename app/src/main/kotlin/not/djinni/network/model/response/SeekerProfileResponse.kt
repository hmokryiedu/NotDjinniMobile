package not.djinni.network.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SeekerProfileResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("speciality")
    val speciality: String,
    @SerialName("experience_years")
    val experienceYears: Int,
    @SerialName("desired_salary")
    val desiredSalary: Int,
    @SerialName("about_me")
    val aboutMe: String?
)
