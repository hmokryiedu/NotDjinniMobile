package not.djinni.data.mapper

import not.djinni.model.SeekerProfile
import not.djinni.network.model.response.SeekerProfileResponse

internal fun SeekerProfileResponse.toDomain(): SeekerProfile {
    return SeekerProfile(
        id = id,
        speciality = speciality,
        experienceYears = experienceYears,
        desiredSalary = desiredSalary,
        aboutMe = aboutMe
    )
}
