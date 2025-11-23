package not.djinni.data.mapper

import not.djinni.model.EmployerProfile
import not.djinni.network.model.response.EmployerProfileResponse

internal fun EmployerProfileResponse.toDomain(): EmployerProfile {
    return EmployerProfile(
        id = id,
        companyId = companyId
    )
}
