package not.djinni.network.employer.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmployerProfileResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("role")
    val role: String,
    @SerialName("company")
    val company: CompanyResponse
)