package not.djinni.network.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmployerProfileResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("company_id")
    val companyId: Long
)
