@file:OptIn(ExperimentalTime::class)

package not.djinni.presentation.screens.seeker.profile.edit

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.asSharedFlow
import not.djinni.R
import not.djinni.core.extension.mutableSideEffect
import not.djinni.domain.repository.SeekerRepository
import not.djinni.model.seeker.SeekerProfile
import not.djinni.model.seeker.WorkExperience
import not.djinni.presentation.core.StateViewModel
import not.djinni.presentation.core.components.base.model.TextData
import not.djinni.presentation.screens.seeker.profile.edit.alert.EditProfileAlert
import org.koin.android.annotation.KoinViewModel
import kotlin.time.ExperimentalTime

@KoinViewModel
internal class EditSeekerProfileViewModel(
    private val seekerRepository: SeekerRepository,
) : StateViewModel<EditSeekerProfileState>(EditSeekerProfileState()) {

    private val _sideEffect = mutableSideEffect<EditSeekerProfileSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()
    private val messages = Channel<TextData>(capacity = Channel.UNLIMITED)

    private var seekerProfileId: Long = 0L
    private var nextNegativeLocalKey: Long = -1L

    init {
        loadProfile()
        collectMessages()
    }

    fun sendAction(action: EditSeekerProfileAction) {
        when (action) {
            EditSeekerProfileAction.NavigateBack -> _sideEffect.tryEmit(EditSeekerProfileSideEffect.NavigateBack)
            EditSeekerProfileAction.Retry -> loadProfile()
            is EditSeekerProfileAction.UpdateSpeciality -> updateState { copy(speciality = action.value) }
            is EditSeekerProfileAction.UpdateDesiredSalary -> updateState { copy(desiredSalary = action.value) }
            is EditSeekerProfileAction.UpdateExperienceYears -> updateState { copy(experienceYears = action.value) }
            is EditSeekerProfileAction.UpdateAboutMe -> updateState { copy(aboutMe = action.value) }
            is EditSeekerProfileAction.SelectJobCategory -> updateState {
                copy(jobCategory = action.category, currentAlert = null)
            }
            EditSeekerProfileAction.ShowSelectJobCategoryAlert -> updateState {
                copy(currentAlert = EditProfileAlert.SELECT_JOB_CATEGORY)
            }
            EditSeekerProfileAction.Save -> saveProfile()
            EditSeekerProfileAction.ShowAddWorkExperience -> {
                updateState {
                    copy(
                        currentAlert = EditProfileAlert.ADD_WORK_EXPERIENCE,
                        editingWorkExperience = EditableWorkExperience(
                            localKey = newNegativeLocalKey(),
                        ),
                    )
                }
            }
            is EditSeekerProfileAction.ShowEditWorkExperience -> {
                val currentItem = state.value.workExperiences.firstOrNull { it.localKey == action.localKey } ?: return
                updateState {
                    copy(
                        currentAlert = EditProfileAlert.EDIT_WORK_EXPERIENCE,
                        editingWorkExperience = currentItem,
                    )
                }
            }
            is EditSeekerProfileAction.RemoveWorkExperience -> {
                updateState { copy(workExperiences = workExperiences.filterNot { it.localKey == action.localKey }) }
            }
            is EditSeekerProfileAction.UpdateEditingCompany -> updateEditing { copy(companyName = action.value) }
            is EditSeekerProfileAction.UpdateEditingPosition -> updateEditing { copy(position = action.value) }
            is EditSeekerProfileAction.UpdateEditingDescription -> updateEditing { copy(description = action.value) }
            is EditSeekerProfileAction.UpdateEditingStartDate -> updateEditing { copy(startDate = action.value) }
            is EditSeekerProfileAction.UpdateEditingEndDate -> updateEditing { copy(endDate = action.value) }
            is EditSeekerProfileAction.UpdateEditingIsCurrent -> updateEditing {
                copy(isCurrent = action.value, endDate = if (action.value) null else endDate ?: startDate)
            }
            EditSeekerProfileAction.SaveWorkExperience -> saveEditingWorkExperience()
            EditSeekerProfileAction.DismissWorkExperienceEditor,
            EditSeekerProfileAction.HideAlert -> updateState {
                copy(currentAlert = null, editingWorkExperience = null)
            }
        }
    }

    private fun loadProfile() {
        launch {
            updateState { copy(isLoading = true, hasError = false) }
            val profile = seekerRepository.getProfile()
            if (profile == null) {
                updateState { copy(isLoading = false, hasError = true) }
                return@launch
            }
            seekerProfileId = profile.id
            updateState {
                copy(
                    isLoading = false,
                    hasError = false,
                    speciality = profile.speciality,
                    desiredSalary = profile.desiredSalary.toString(),
                    experienceYears = profile.experienceYears.toString(),
                    aboutMe = profile.aboutMe.orEmpty(),
                    jobCategory = profile.jobCategory,
                    workExperiences = profile.workExperience.map {
                        EditableWorkExperience(
                            localKey = it.id ?: newNegativeLocalKey(),
                            id = it.id,
                            companyName = it.companyName,
                            position = it.position,
                            description = it.description,
                            startDate = it.startDate,
                            endDate = it.endDate,
                            isCurrent = it.isCurrent,
                        )
                    },
                )
            }
        }
    }

    private fun saveProfile() {
        val current = state.value
        val category = current.jobCategory ?: return
        val desiredSalary = current.desiredSalary.toIntOrNull() ?: return
        val experienceYears = current.experienceYears.toIntOrNull() ?: return
        if (current.speciality.isBlank()) return
        launch {
            updateState { copy(isSaving = true) }
            val updatedProfile = SeekerProfile(
                id = seekerProfileId,
                speciality = current.speciality,
                desiredSalary = desiredSalary,
                experienceYears = experienceYears,
                aboutMe = current.aboutMe.ifBlank { null },
                jobCategory = category,
                workExperience = current.workExperiences.map {
                    WorkExperience(
                        id = it.id,
                        companyName = it.companyName,
                        position = it.position,
                        description = it.description,
                        startDate = it.startDate ?: return@launch,
                        endDate = if (it.isCurrent) null else it.endDate,
                        isCurrent = it.isCurrent,
                    )
                },
            )
            seekerRepository.updateProfile(updatedProfile)
            updateState { copy(isSaving = false) }
            _sideEffect.emit(EditSeekerProfileSideEffect.NavigateBack)
        }
    }

    private fun saveEditingWorkExperience() {
        val editing = state.value.editingWorkExperience ?: return
        val errorResId = editing.getValidationErrorResId()
        if (errorResId != null) {
            messages.trySend(TextData.Resource(errorResId))
            return
        }
        val startDate = editing.startDate
        val endDate = editing.endDate
        updateState {
            val existingIndex = workExperiences.indexOfFirst { it.localKey == editing.localKey }
            val updated = if (existingIndex >= 0) {
                workExperiences.toMutableList().apply { set(existingIndex, editing) }
            } else {
                workExperiences + editing
            }
            copy(
                workExperiences = updated,
                editingWorkExperience = null,
                currentAlert = null,
            )
        }
    }

    private fun updateEditing(update: EditableWorkExperience.() -> EditableWorkExperience) {
        val current = state.value.editingWorkExperience ?: return
        updateState { copy(editingWorkExperience = current.update()) }
    }

    private fun newNegativeLocalKey(): Long {
        val key = nextNegativeLocalKey
        nextNegativeLocalKey -= 1
        return key
    }

    private fun collectMessages() {
        launch {
            messages.consumeAsFlow().collect { message ->
                updateState { copy(message = message) }
                delay(MESSAGE_DISPLAY_DURATION_MS)
                updateState { copy(message = null) }
            }
        }
    }

    private fun EditableWorkExperience.getValidationErrorResId(): Int? {
        return when {
            position.isBlank() -> R.string.position_should_not_be_empty
            companyName.isBlank() -> R.string.company_name_should_not_be_empty
            startDate == null -> R.string.date_range_has_to_selected
            !isCurrent && endDate == null -> R.string.date_range_has_to_selected
            !isCurrent && endDate != null && endDate <= startDate -> R.string.end_date_has_to_be_after_start_date
            else -> null
        }
    }

    private companion object {
        const val MESSAGE_DISPLAY_DURATION_MS = 3000L
    }
}
