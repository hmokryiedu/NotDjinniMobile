@file:OptIn(ExperimentalTime::class)

package not.djinni.data.mapper

import not.djinni.model.seeker.SeekerProfile
import not.djinni.model.seeker.WorkExperience
import not.djinni.model.seeker.vacancy.toDomain
import not.djinni.model.seeker.vacancy.toResponse
import not.djinni.network.seeker.request.CreateSeekerProfileRequest
import not.djinni.network.seeker.request.CreateWorkExperienceRequest
import not.djinni.network.seeker.response.SeekerProfileResponse
import kotlin.time.ExperimentalTime

internal fun SeekerProfileResponse.toDomain(): SeekerProfile {
    return SeekerProfile(
        id = id,
        speciality = speciality,
        experienceYears = experienceYears,
        desiredSalary = desiredSalary,
        aboutMe = aboutMe,
        jobCategory = jobCategory.toDomain()
    )
}

internal fun SeekerProfile.toRequest(): CreateSeekerProfileRequest {
    return CreateSeekerProfileRequest(
        speciality = speciality,
        experienceYears = experienceYears,
        desiredSalary = desiredSalary,
        aboutMe = aboutMe,
        jobCategory = jobCategory.toResponse(),
        workExperience = workExperience.map(WorkExperience::toRequest)
    )
}

internal fun WorkExperience.toRequest(): CreateWorkExperienceRequest {
    return CreateWorkExperienceRequest(
        companyName = companyName,
        position = position,
        startDate = startDate,
        endDate = endDate,
        description = description
    )
}
