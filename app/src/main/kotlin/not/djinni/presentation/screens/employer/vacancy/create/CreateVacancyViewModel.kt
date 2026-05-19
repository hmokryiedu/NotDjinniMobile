package not.djinni.presentation.screens.employer.vacancy.create

import kotlinx.coroutines.flow.asSharedFlow
import not.djinni.R
import not.djinni.core.extension.mutableSideEffect
import not.djinni.domain.repository.VacancyRepository
import not.djinni.domain.usecase.vacancy.CreateVacancyUseCase
import not.djinni.presentation.core.StateViewModel
import not.djinni.presentation.core.components.base.model.SnackBarData
import not.djinni.presentation.core.extension.toTextData
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam

@KoinViewModel
internal class CreateVacancyViewModel(
    @InjectedParam private val sourceVacancyId: Long?,
    private val createVacancyUseCase: CreateVacancyUseCase,
    private val vacancyRepository: VacancyRepository,
) : StateViewModel<CreateVacancyState>(CreateVacancyState()) {

    private val _sideEffect = mutableSideEffect<CreateVacancySideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        prefillFromSourceVacancy()
    }

    fun sendAction(action: CreateVacancyAction) {
        when (action) {
            CreateVacancyAction.NavigateBack -> navigateBack()
            CreateVacancyAction.ShowEmploymentTypeSheet -> updateState { copy(alert = CreateVacancyAlert.SelectEmploymentType) }
            CreateVacancyAction.ShowCategorySheet -> updateState { copy(alert = CreateVacancyAlert.SelectCategory) }
            CreateVacancyAction.HideAlert -> updateState { copy(alert = null) }
            is CreateVacancyAction.SelectEmploymentType -> updateState {
                copy(selectedEmploymentType = action.type, alert = null)
            }

            is CreateVacancyAction.SelectCategory -> updateState {
                copy(selectedCategory = action.category, alert = null)
            }

            is CreateVacancyAction.SubmitVacancy -> submitVacancy(action)
        }
    }

    private fun prefillFromSourceVacancy() {
        val id = sourceVacancyId ?: return
        launch {
            vacancyRepository.getVacancyById(id).onSuccess { vacancy ->
                updateState {
                    copy(
                        title = vacancy.title,
                        description = vacancy.description,
                        salaryMin = vacancy.salaryMin.toString(),
                        salaryMax = vacancy.salaryMax.toString(),
                        experienceYears = (vacancy.minExperienceYears ?: 0).toString(),
                        selectedEmploymentType = vacancy.employmentType,
                        selectedCategory = vacancy.category,
                        prefillVersion = prefillVersion + 1,
                    )
                }
            }
        }
    }

    private fun navigateBack() {
        launch {
            _sideEffect.emit(CreateVacancySideEffect.NavigateBack)
        }
    }

    private fun submitVacancy(data: CreateVacancyAction.SubmitVacancy) {
        launch(loadingEnabled = true) {
            val currentState = mutableState.value
            val employmentType = currentState.selectedEmploymentType
            val category = currentState.selectedCategory
            if (data.title.isEmpty() || data.description.isEmpty() || employmentType == null || category == null) {
                showSnackBar(SnackBarData(message = R.string.create_vacancy_fill_all_fields.toTextData()))
                return@launch
            }
            val salaryMin = data.salaryMin.toIntOrNull()
            val salaryMax = data.salaryMax.toIntOrNull()
            val experienceYears = data.experienceYears.toIntOrNull()
            if (salaryMin == null || salaryMax == null || experienceYears == null) {
                showSnackBar(SnackBarData(message = R.string.create_vacancy_invalid_numbers.toTextData()))
                return@launch
            }
            createVacancyUseCase(
                CreateVacancyUseCase.Params(
                    title = data.title,
                    description = data.description,
                    salaryMin = salaryMin,
                    salaryMax = salaryMax,
                    experienceYears = experienceYears,
                    employmentType = employmentType,
                    category = category
                )
            ).onSuccess { vacancy ->
                _sideEffect.emit(CreateVacancySideEffect.NavigateToDetails(vacancy.id))
            }.onFailure { error ->
                val data = SnackBarData(
                    message = (error.message ?: "Failed to create vacancy").toTextData()
                )
                showSnackBar(data)
            }
        }
    }
}
