package not.djinni.data.mapper

import not.djinni.model.company.Company
import not.djinni.network.employer.response.CompanyResponse

fun CompanyResponse.toDomain(): Company {
    return Company(
        id = id,
        name = name,
        description = description,
        website = website,
    )
}