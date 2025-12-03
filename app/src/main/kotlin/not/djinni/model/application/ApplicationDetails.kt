@file:OptIn(ExperimentalTime::class)

package not.djinni.model.application

import not.djinni.model.seeker.SeekerProfile
import not.djinni.model.seeker.vacancy.Vacancy
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

data class ApplicationDetails(
    val id: Long,
    val vacancy: Vacancy,
    val jobSeeker: SeekerProfile,
    val status: ApplicationStatus,
    val coverLetter: String?,
    val createdAt: Instant,
    val updatedAt: Instant
)
