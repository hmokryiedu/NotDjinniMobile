@file:OptIn(ExperimentalTime::class)

package not.djinni.data.mapper

import not.djinni.model.application.ApplicationDetails
import not.djinni.model.application.ApplicationStatus
import not.djinni.model.seeker.vacancy.toDomain
import not.djinni.network.application.request.ApplicationStatusRequest
import not.djinni.network.application.response.ApplicationDetailsResponse
import not.djinni.network.application.response.ApplicationStatusResponse
import kotlin.time.ExperimentalTime

internal fun ApplicationDetailsResponse.toDomain(): ApplicationDetails = ApplicationDetails(
    id = id,
    vacancy = vacancy.toDomain(),
    jobSeeker = jobSeeker.toDomain(),
    status = status.toDomain(),
    coverLetter = coverLetter,
    createdAt = createdAt,
    updatedAt = updatedAt
)

internal fun ApplicationStatusResponse.toDomain(): ApplicationStatus = when (this) {
    ApplicationStatusResponse.APPLIED -> ApplicationStatus.APPLIED
    ApplicationStatusResponse.REVIEWING -> ApplicationStatus.REVIEWING
    ApplicationStatusResponse.INTERVIEW -> ApplicationStatus.INTERVIEW
    ApplicationStatusResponse.TEST_TASK -> ApplicationStatus.TEST_TASK
    ApplicationStatusResponse.OFFER -> ApplicationStatus.OFFER
    ApplicationStatusResponse.HIRED -> ApplicationStatus.HIRED
    ApplicationStatusResponse.REJECTED -> ApplicationStatus.REJECTED
    ApplicationStatusResponse.WITHDRAWN -> ApplicationStatus.WITHDRAWN
}

internal fun ApplicationStatus.toRequest(): ApplicationStatusRequest = when (this) {
    ApplicationStatus.APPLIED -> ApplicationStatusRequest.APPLIED
    ApplicationStatus.REVIEWING -> ApplicationStatusRequest.REVIEWING
    ApplicationStatus.INTERVIEW -> ApplicationStatusRequest.INTERVIEW
    ApplicationStatus.TEST_TASK -> ApplicationStatusRequest.TEST_TASK
    ApplicationStatus.OFFER -> ApplicationStatusRequest.OFFER
    ApplicationStatus.HIRED -> ApplicationStatusRequest.HIRED
    ApplicationStatus.REJECTED -> ApplicationStatusRequest.REJECTED
    ApplicationStatus.WITHDRAWN -> ApplicationStatusRequest.WITHDRAWN
}
