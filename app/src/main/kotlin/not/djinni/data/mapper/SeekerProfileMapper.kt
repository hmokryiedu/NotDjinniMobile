@file:OptIn(ExperimentalTime::class)

package not.djinni.data.mapper

import not.djinni.model.seeker.SeekerProfile
import not.djinni.model.seeker.WorkExperience
import not.djinni.model.seeker.vacancy.toDomain
import not.djinni.model.seeker.vacancy.toResponse
import not.djinni.network.seeker.request.CreateSeekerProfileRequest
import not.djinni.network.seeker.request.CreateWorkExperienceRequest
import not.djinni.network.seeker.request.UpdateSeekerProfileRequest
import not.djinni.network.seeker.request.UpdateWorkExperienceRequest
import not.djinni.network.seeker.response.SeekerProfileResponse
import not.djinni.network.seeker.response.WorkExperienceResponse
import kotlin.time.ExperimentalTime

internal fun SeekerProfileResponse.toDomain(): SeekerProfile {
    return SeekerProfile(
        id = id,
        speciality = speciality,
        experienceYears = experienceYears,
        desiredSalary = desiredSalary,
        aboutMe = aboutMe,
        jobCategory = jobCategory.toDomain(),
        workExperience = workExperience.map(WorkExperienceResponse::toDomain),
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
        endDate = endDate ?: startDate,
        description = description
    )
}

internal fun SeekerProfile.toUpdateRequest(): UpdateSeekerProfileRequest {
    return UpdateSeekerProfileRequest(
        speciality = speciality,
        experienceYears = experienceYears,
        desiredSalary = desiredSalary,
        aboutMe = aboutMe,
        jobCategory = jobCategory.toResponse(),
        workExperience = workExperience.map(WorkExperience::toUpdateRequest),
    )
}

internal fun WorkExperience.toUpdateRequest(): UpdateWorkExperienceRequest {
    return UpdateWorkExperienceRequest(
        id = id,
        companyName = companyName,
        position = position,
        description = description,
        startDate = startDate,
        endDate = endDate,
        isCurrent = isCurrent,
    )
}

internal fun WorkExperienceResponse.toDomain(): WorkExperience {
    return WorkExperience(
        id = id,
        companyName = companyName,
        position = position,
        description = description,
        startDate = startDate,
        endDate = endDate,
        isCurrent = isCurrent,
    )
}
