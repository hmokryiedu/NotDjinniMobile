package not.djinni.presentation.screens.seeker.profile.create.model

import androidx.compose.runtime.Stable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Stable
data class WorkExperienceData(
    val position: String,
    val companyName: String,
    val description: String?,
    val endDate: Instant,
    val startDate: Instant,
)