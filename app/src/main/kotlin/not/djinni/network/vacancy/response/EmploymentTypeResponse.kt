package not.djinni.network.vacancy.response

import kotlinx.serialization.Serializable

@Serializable
enum class EmploymentTypeResponse {
    FULL_TIME,
    PART_TIME,
    CONTRACT,
    TEMPORARY,
    INTERNSHIP,
    FREELANCE
}