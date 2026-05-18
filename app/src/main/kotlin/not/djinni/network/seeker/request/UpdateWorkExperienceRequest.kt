package not.djinni.network.seeker.request

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Serializable
data class UpdateWorkExperienceRequest(
    @SerialName("id")
    val id: Long? = null,
    @SerialName("company_name")
    val companyName: String,
    @SerialName("position")
    val position: String,
    @SerialName("description")
    val description: String?,
    @SerialName("start_date")
    @Contextual
    val startDate: Instant,
    @SerialName("end_date")
    @Contextual
    val endDate: Instant?,
    @SerialName("is_current")
    val isCurrent: Boolean,
)
