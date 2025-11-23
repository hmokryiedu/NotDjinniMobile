package not.djinni.network.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    @SerialName("token")
    val token: String,
    @SerialName("user")
    val user: UserResponse
)
