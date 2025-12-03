@file:OptIn(ExperimentalTime::class)

package not.djinni.network.application.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
data class ApplicationResponse(
    @SerialName("id")
    val id: Long,
    @SerialName("vacancy_id")
    val vacancyId: Long,
    @SerialName("job_seeker_id")
    val jobSeekerId: Long,
    @SerialName("status")
    val status: ApplicationStatusResponse,
    @SerialName("cover_letter")
    val coverLetter: String?,
    @SerialName("created_at")
    val createdAt: Instant,
    @SerialName("updated_at")
    val updatedAt: Instant
)

@Serializable
enum class ApplicationStatusResponse {
    @SerialName("applied")
    APPLIED,
    @SerialName("reviewing")
    REVIEWING,
    @SerialName("interview")
    INTERVIEW,
    @SerialName("test_task")
    TEST_TASK,
    @SerialName("offer")
    OFFER,
    @SerialName("hired")
    HIRED,
    @SerialName("rejected")
    REJECTED,
    @SerialName("withdrawn")
    WITHDRAWN
}
