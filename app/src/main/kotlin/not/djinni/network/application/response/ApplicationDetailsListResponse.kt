package not.djinni.network.application.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApplicationDetailsListResponse(
    @SerialName("applications")
    val applications: List<ApplicationDetailsResponse>
)
