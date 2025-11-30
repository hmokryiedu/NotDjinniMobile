package not.djinni.network.employer.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CompanyResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("company_name")
    val name: String,
    @SerialName("website")
    val website: String?,
    @SerialName("description")
    val description: String
)
