package not.djinni.model.employer

import not.djinni.model.company.Company

data class EmployerProfile(
    val id: Long,
    val role: String,
    val company: Company
)