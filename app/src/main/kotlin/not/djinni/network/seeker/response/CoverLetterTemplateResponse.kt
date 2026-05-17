package not.djinni.network.seeker.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoverLetterTemplateResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("message")
    val message: String,
)
