package not.djinni.presentation.screens.onboarding

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import not.djinni.core.extension.mutableSideEffect
import not.djinni.core.extension.orFalse
import not.djinni.model.EmploymentType
import not.djinni.model.JobCategory
import not.djinni.model.Location
import not.djinni.presentation.core.StateViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class OnboardingViewModel : StateViewModel<OnboardingState>(OnboardingState()) {

    private val _sideEffect = mutableSideEffect<OnboardingSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private val personalInformation = MutableStateFlow<PersonalInformation?>(null)
    private val workPreferences = MutableStateFlow<WorkPreferences?>(null)

    private val availableLocations = listOf(
        Location(id = "remote", name = "Remote"),
        Location(id = "hybrid", name = "Hybrid"),
        Location(id = "onsite", name = "On-site")
    )

    private val availableEmploymentTypes = EmploymentType.entries

    private val availableJobCategories = listOf(
        JobCategory(id = "software_engineer", name = "Software Engineer"),
        JobCategory(id = "designer", name = "Designer"),
        JobCategory(id = "product_manager", name = "Product Manager"),
        JobCategory(id = "data_scientist", name = "Data Scientist"),
        JobCategory(id = "devops_engineer", name = "DevOps Engineer"),
        JobCategory(id = "qa_engineer", name = "QA Engineer")
    )

    init {
        collectIsButtonEnabled()
    }

    fun onAction(action: OnboardingAction) {
        when (action) {
            is OnboardingAction.PersonalInformationChanged -> {
                setPersonalInformation(fullName = action.fullName, aboutMe = action.aboutMe)
            }

            is OnboardingAction.WorkPreferencesChanged -> {
                setWorkPreferences(
                    jobCategory = action.jobCategory,
                    yearsOfExperience = action.yearsOfExperience,
                    employmentType = action.employmentType,
                    workLocation = action.workLocation,
                    salaryExpectations = action.salaryExpectations
                )
            }

            OnboardingAction.ShowJobCategoryAlert -> showJobCategoryAlert()
            OnboardingAction.ShowEmploymentTypeAlert -> showEmploymentAlert()
            OnboardingAction.ShowLocationAlert -> showLocationAlert()
            OnboardingAction.DismissAlert -> updateState { copy(currentAlert = null) }
            is OnboardingAction.SelectJobCategory -> updateState { copy(jobCategory = action.jobCategory) }
            is OnboardingAction.SelectEmploymentType -> updateState { copy(employmentType = action.employmentType) }
            is OnboardingAction.SelectLocation -> updateState { copy(location = action.location) }
            OnboardingAction.Continue -> onContinue()
        }
    }

    private fun onContinue() {
        launch {
            val step = when (state.value.step) {
                Step.PERSONAL_INFORMATION -> Step.JOB_PREFERENCES
                Step.JOB_PREFERENCES -> Step.COMPLETED
                Step.COMPLETED -> return@launch
            }
            updateState { copy(step = step) }
        }
    }

    private fun setPersonalInformation(fullName: String, aboutMe: String) {
        personalInformation.update {
            PersonalInformation(fullName = fullName, aboutMe = aboutMe)
        }
    }

    private fun setWorkPreferences(
        jobCategory: String,
        yearsOfExperience: String,
        employmentType: String,
        workLocation: String,
        salaryExpectations: String
    ) {
        workPreferences.update {
            WorkPreferences(
                jobCategory = jobCategory,
                yearsOfExperience = yearsOfExperience,
                employmentType = employmentType,
                workLocation = workLocation,
                salaryExpectations = salaryExpectations
            )
        }
    }

    private fun showLocationAlert() {
        updateState {
            val alert = OnboardingAlert.LocationSelection(
                locations = availableLocations,
                selected = location
            )
            copy(currentAlert = alert)
        }
    }

    private fun showEmploymentAlert() {
        updateState {
            val alert = OnboardingAlert.EmploymentTypeSelection(
                types = availableEmploymentTypes,
                selected = employmentType
            )
            copy(currentAlert = alert)
        }
    }

    private fun showJobCategoryAlert() {
        updateState {
            val alert = OnboardingAlert.JobCategorySelection(
                categories = availableJobCategories,
                selected = jobCategory
            )
            copy(currentAlert = alert)
        }
    }

    private fun collectIsButtonEnabled() {
        launch {
            combine(
                state,
                personalInformation,
                workPreferences
            ) { state, personalInformation, workPreferences ->
                when (state.step) {
                    Step.PERSONAL_INFORMATION -> personalInformation?.isValid().orFalse()
                    Step.JOB_PREFERENCES -> workPreferences?.isValid().orFalse()
                    Step.COMPLETED -> true
                }
            }.collectLatest { isButtonEnabled ->
                updateState { copy(buttonData = buttonData.copy(enabled = isButtonEnabled)) }
            }
        }
    }

    private data class PersonalInformation(val fullName: String, val aboutMe: String) {
        fun isValid() = fullName.isNotBlank()
    }

    private data class WorkPreferences(
        val jobCategory: String,
        val workLocation: String,
        val employmentType: String,
        val yearsOfExperience: String,
        val salaryExpectations: String
    ) {
        fun isValid() = jobCategory.isNotBlank() &&
                yearsOfExperience.toIntOrNull()?.let { it > 0 } == true &&
                employmentType.isNotBlank() &&
                workLocation.isNotBlank() &&
                salaryExpectations.toIntOrNull()?.let { it > 0 } == true
    }
}
