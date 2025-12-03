package not.djinni.network.application.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateApplicationStatusRequest(
    @SerialName("status")
    val status: ApplicationStatusRequest,
)

@Serializable
enum class ApplicationStatusRequest {
    APPLIED,
    REVIEWING,
    INTERVIEW,
    TEST_TASK,
    OFFER,
    HIRED,
    REJECTED,
    WITHDRAWN
}
