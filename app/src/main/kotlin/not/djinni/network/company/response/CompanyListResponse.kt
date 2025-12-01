package not.djinni.network.company.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import not.djinni.network.employer.response.CompanyResponse

@Serializable
data class CompanyListResponse(
    @SerialName("companies")
    val companies: List<CompanyResponse>
)
