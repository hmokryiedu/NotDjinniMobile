package not.djinni.presentation.screens.seeker.profile.create

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.consumeAsFlow
import not.djinni.R
import not.djinni.core.extension.mutableSideEffect
import not.djinni.core.extension.orZero
import not.djinni.domain.repository.SeekerRepository
import not.djinni.model.seeker.SeekerProfile
import not.djinni.model.seeker.WorkExperience
import not.djinni.presentation.core.StateViewModel
import not.djinni.presentation.core.components.base.model.TextData
import not.djinni.presentation.screens.seeker.profile.create.alert.CreateProfileAlert
import not.djinni.presentation.screens.seeker.profile.create.model.WorkExperienceData
import org.koin.android.annotation.KoinViewModel
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@KoinViewModel
internal class CreateSeekerProfileViewModel(
    private val seekerRepository: SeekerRepository,
) : StateViewModel<CreateSeekerProfileState>(
    CreateSeekerProfileState()
) {
    private val _sideEffect = mutableSideEffect<CreateSeekerProfileSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private val messages = Channel<TextData>(capacity = Channel.UNLIMITED)

    private val _workExperiences = mutableSideEffect<List<WorkExperienceData>>()

    init {
        collectWorkExperiences()
        collectMessages()
    }

    fun sendAction(action: CreateSeekerProfileAction) {
        when (action) {
            CreateSeekerProfileAction.ShowAddWorkExperienceAlert -> {
                updateState { copy(currentAlert = CreateProfileAlert.ADD_WORK_EXPERIENCE) }
            }
            CreateSeekerProfileAction.ShowSelectJobCategoryAlert -> {
                updateState { copy(currentAlert = CreateProfileAlert.SELECT_JOB_CATEGORY) }
            }
            is CreateSeekerProfileAction.SelectJobCategory -> {
                updateState { copy(selectedJobCategory = action.category) }
                hideAlert()
            }
            is CreateSeekerProfileAction.CreateProfile -> createProfile(action)
            is CreateSeekerProfileAction.AddWorkExperience -> addWorkExperience(action)
            CreateSeekerProfileAction.HideAlert -> hideAlert()
        }
    }

    private fun addWorkExperience(data: CreateSeekerProfileAction.AddWorkExperience) {
        val errorResId = data.getValidationErrorResId()
        if (errorResId != null) {
            messages.trySend(TextData.Resource(errorResId))
            return
        }
        hideAlert()
        val data = WorkExperienceData(
            position = data.position,
            companyName = data.companyName,
            description = data.description,
            startDate = data.startDate ?: return,
            endDate = data.endDate ?: return
        )
        updateState { copy(workExperiences = workExperiences + data) }
    }

    private fun createProfile(data: CreateSeekerProfileAction.CreateProfile) {
        launch {
            val errorResId = data.getValidationErrorResId()
            if (errorResId != null) {
                messages.trySend(TextData.Resource(errorResId))
                return@launch
            }
            val selectedCategory = state.value.selectedJobCategory
            if (selectedCategory == null) {
                messages.trySend(TextData.Resource(R.string.job_category_should_not_be_empty))
                return@launch
            }
            val profile = SeekerProfile(
                id = 0,
                speciality = data.speciality,
                desiredSalary = data.desiredSalary.toIntOrNull().orZero(),
                experienceYears = data.yearsOfExperience.toIntOrNull().orZero(),
                aboutMe = data.aboutMe,
                jobCategory = selectedCategory,
                workExperience = state.value.workExperiences.map {
                    WorkExperience(
                        id = null,
                        position = it.position,
                        companyName = it.companyName,
                        description = it.description,
                        startDate = it.startDate,
                        endDate = it.endDate,
                        isCurrent = false,
                    )
                }
            )
            seekerRepository.createProfile(profile = profile)
            _sideEffect.emit(CreateSeekerProfileSideEffect.NavigateToHome)
        }
    }

    private fun hideAlert() {
        updateState { copy(currentAlert = null) }
    }

    private fun collectWorkExperiences() {
        launch {
            _workExperiences.collectLatest { workExperiences ->
                updateState { copy(workExperiences = workExperiences) }
            }
        }
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

    private fun CreateSeekerProfileAction.AddWorkExperience.getValidationErrorResId(): Int? {
        return when {
            position.isBlank() -> R.string.position_should_not_be_empty
            companyName.isBlank() -> R.string.company_name_should_not_be_empty
            startDate == null || endDate == null -> R.string.date_range_has_to_selected
            startDate >= endDate -> R.string.end_date_has_to_be_after_start_date
            else -> null
        }
    }

    private fun CreateSeekerProfileAction.CreateProfile.getValidationErrorResId(): Int? {
        return when {
            speciality.isBlank() -> R.string.speciality_should_not_be_empty
            yearsOfExperience.isBlank() -> R.string.years_of_experience_should_not_be_empty
            desiredSalary.isBlank() -> R.string.desired_salary_should_not_be_empty
            else -> null
        }
    }

    private companion object {
        const val MESSAGE_DISPLAY_DURATION_MS = 2000L
    }
}
