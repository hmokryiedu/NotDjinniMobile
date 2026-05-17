package not.djinni.network.seeker.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateCoverLetterTemplateRequest(
    @SerialName("message")
    val message: String,
)
