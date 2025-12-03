@file:OptIn(ExperimentalTime::class)

package not.djinni.network.application.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import not.djinni.network.seeker.response.SeekerProfileResponse
import not.djinni.network.vacancy.response.VacancyDetailsResponse
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
data class ApplicationDetailsResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("vacancy")
    val vacancy: VacancyDetailsResponse,
    @SerialName("job_seeker")
    val jobSeeker: SeekerProfileResponse,
    @SerialName("status")
    val status: ApplicationStatusResponse,
    @SerialName("cover_letter")
    val coverLetter: String?,
    @SerialName("created_at")
    val createdAt: Instant,
    @SerialName("updated_at")
    val updatedAt: Instant
)
