package not.djinni.model.seeker

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
data class WorkExperience(
    val id: Long,
    val companyName: String,
    val position: String,
    val description: String?,
    val startDate: Instant,
    val endDate: Instant
)