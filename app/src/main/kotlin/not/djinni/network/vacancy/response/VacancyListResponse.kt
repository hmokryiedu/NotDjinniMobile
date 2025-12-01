package not.djinni.network.vacancy.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VacancyListResponse(
    @SerialName("vacancies")
    val vacancies: List<VacancyDetailsResponse>
)
