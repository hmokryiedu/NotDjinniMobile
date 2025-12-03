package not.djinni.presentation.core.components.base.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class ApplicationCardData(
    val id: Long,
    val speciality: TextData,
    val experienceYears: TextData,
    val status: TextData,
    val statusColor: Color,
    val coverLetterPreview: TextData?,
    val appliedDate: TextData,
)
