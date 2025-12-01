package not.djinni.presentation.screens.seeker.profile.create

import androidx.compose.runtime.Immutable
import not.djinni.presentation.core.components.base.model.TextData
import not.djinni.presentation.screens.seeker.profile.create.alert.CreateProfileAlert
import not.djinni.presentation.screens.seeker.profile.create.model.WorkExperienceData

@Immutable
internal data class CreateSeekerProfileState(
    val message: TextData? = null,
    val currentAlert: CreateProfileAlert? = null,
    val workExperiences: List<WorkExperienceData> = emptyList(),
)
