package not.djinni.network.seeker.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CoverLetterTemplateListResponse(
    @SerialName("templates")
    val templates: List<CoverLetterTemplateResponse>,
)
