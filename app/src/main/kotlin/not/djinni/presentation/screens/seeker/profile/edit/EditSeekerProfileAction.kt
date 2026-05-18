package not.djinni.presentation.screens.seeker.profile.edit

import not.djinni.model.seeker.vacancy.JobCategoryCode
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
internal sealed interface EditSeekerProfileAction {
    data object NavigateBack : EditSeekerProfileAction
    data object Save : EditSeekerProfileAction
    data object Retry : EditSeekerProfileAction
    data object HideAlert : EditSeekerProfileAction
    data class UpdateSpeciality(val value: String) : EditSeekerProfileAction
    data class UpdateDesiredSalary(val value: String) : EditSeekerProfileAction
    data class UpdateExperienceYears(val value: String) : EditSeekerProfileAction
    data class UpdateAboutMe(val value: String) : EditSeekerProfileAction
    data class SelectJobCategory(val category: JobCategoryCode) : EditSeekerProfileAction
    data object ShowSelectJobCategoryAlert : EditSeekerProfileAction
    data object ShowAddWorkExperience : EditSeekerProfileAction
    data class ShowEditWorkExperience(val localKey: Long) : EditSeekerProfileAction
    data class RemoveWorkExperience(val localKey: Long) : EditSeekerProfileAction
    data class UpdateEditingCompany(val value: String) : EditSeekerProfileAction
    data class UpdateEditingPosition(val value: String) : EditSeekerProfileAction
    data class UpdateEditingDescription(val value: String) : EditSeekerProfileAction
    data class UpdateEditingStartDate(val value: Instant?) : EditSeekerProfileAction
    data class UpdateEditingEndDate(val value: Instant?) : EditSeekerProfileAction
    data class UpdateEditingIsCurrent(val value: Boolean) : EditSeekerProfileAction
    data object SaveWorkExperience : EditSeekerProfileAction
    data object DismissWorkExperienceEditor : EditSeekerProfileAction
}
