package not.djinni.data.mapper

import not.djinni.model.employer.EmployerProfile
import not.djinni.network.employer.response.EmployerProfileResponse

internal fun EmployerProfileResponse.toDomain(): EmployerProfile {
    return EmployerProfile(
        id = id,
        role = role,
        company = company.toDomain()
    )
}
