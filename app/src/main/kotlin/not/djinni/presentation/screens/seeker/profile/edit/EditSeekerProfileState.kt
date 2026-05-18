package not.djinni.presentation.screens.seeker.profile.edit

import androidx.compose.runtime.Immutable
import not.djinni.model.seeker.vacancy.JobCategoryCode
import not.djinni.presentation.core.components.base.model.TextData
import not.djinni.presentation.screens.seeker.profile.edit.alert.EditProfileAlert
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Immutable
internal data class EditSeekerProfileState(
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val hasError: Boolean = false,
    val speciality: String = "",
    val desiredSalary: String = "",
    val experienceYears: String = "",
    val aboutMe: String = "",
    val jobCategory: JobCategoryCode? = null,
    val currentAlert: EditProfileAlert? = null,
    val workExperiences: List<EditableWorkExperience> = emptyList(),
    val editingWorkExperience: EditableWorkExperience? = null,
    val message: TextData? = null,
)

@OptIn(ExperimentalTime::class)
@Immutable
internal data class EditableWorkExperience(
    val localKey: Long = 0L,
    val id: Long? = null,
    val companyName: String = "",
    val position: String = "",
    val description: String? = null,
    val startDate: Instant? = null,
    val endDate: Instant? = null,
    val isCurrent: Boolean = false,
)
