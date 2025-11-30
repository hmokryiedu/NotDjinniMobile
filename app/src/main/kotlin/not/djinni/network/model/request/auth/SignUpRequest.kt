package not.djinni.network.model.request.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    @SerialName("name")
    val name: String,
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String,
)