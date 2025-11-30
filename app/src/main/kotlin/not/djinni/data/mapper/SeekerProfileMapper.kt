package not.djinni.data.mapper

import not.djinni.model.seeker.SeekerProfile
import not.djinni.network.seeker.response.SeekerProfileResponse

internal fun SeekerProfileResponse.toDomain(): SeekerProfile {
    return SeekerProfile(
        id = id,
        speciality = speciality,
        experienceYears = experienceYears,
        desiredSalary = desiredSalary,
        aboutMe = aboutMe
    )
}
