package not.djinni.network.vacancy.response

import kotlinx.serialization.Serializable

@Serializable
enum class VacancyStatusCodeResponse {
    DRAFT,
    ACTIVE,
    PAUSED,
    CLOSED,
    EXPIRED
}