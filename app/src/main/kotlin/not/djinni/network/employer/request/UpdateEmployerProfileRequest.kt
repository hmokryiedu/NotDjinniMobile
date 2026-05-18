package not.djinni.network.employer.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateEmployerProfileRequest(
    @SerialName("role")
    val role: String,
)
