package not.djinni.presentation.screens.employer.application.details

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import not.djinni.presentation.core.components.base.model.TextData

@Immutable
internal data class ApplicationDetailsState(
    val contentState: ContentState = ContentState.Loading,
    val openedAlert: ApplicationDetailsAlert? = null,
)

internal sealed interface ContentState {
    data object Loading : ContentState
    data class Error(val message: TextData) : ContentState
    data class Data(val details: ApplicationDetailsDisplayData) : ContentState
}

@Immutable
internal data class ApplicationDetailsDisplayData(
    val applicationId: Long,
    val currentStatus: not.djinni.model.application.ApplicationStatus,
    val status: TextData,
    val statusColor: Color,
    val seekerName: TextData,
    val seekerSpeciality: TextData,
    val seekerExperience: TextData,
    val vacancyTitle: TextData,
    val vacancySalary: TextData,
    val vacancyEmploymentType: TextData,
    val coverLetter: TextData?,
    val appliedDate: TextData,
    val updatedDate: TextData,
    val vacancyId: Long,
)
