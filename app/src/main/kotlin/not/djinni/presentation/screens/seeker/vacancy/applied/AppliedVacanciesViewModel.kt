package not.djinni.presentation.screens.seeker.vacancy.applied

import kotlinx.coroutines.flow.asSharedFlow
import not.djinni.R
import not.djinni.core.extension.mutableSideEffect
import not.djinni.domain.repository.VacancyRepository
import not.djinni.model.seeker.vacancy.Vacancy
import not.djinni.presentation.core.StateViewModel
import not.djinni.presentation.core.components.base.model.VacancyCardData
import not.djinni.presentation.core.extension.toDisplayName
import not.djinni.presentation.core.extension.toTextData
import not.djinni.utils.string.StringProvider
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class AppliedVacanciesViewModel(
    private val stringProvider: StringProvider,
    private val vacancyRepository: VacancyRepository,
) : StateViewModel<AppliedVacanciesState>(
    AppliedVacanciesState()
) {

    private val _sideEffect = mutableSideEffect<AppliedVacanciesSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        loadVacancies()
    }

    fun sendAction(action: AppliedVacanciesAction) {
        when (action) {
            AppliedVacanciesAction.Load -> loadVacancies()
            AppliedVacanciesAction.NavigateBack -> _sideEffect.tryEmit(AppliedVacanciesSideEffect.NavigateBack)
            is AppliedVacanciesAction.OpenVacancy -> _sideEffect.tryEmit(
                AppliedVacanciesSideEffect.NavigateToVacancyDetails(action.vacancyId)
            )
        }
    }

    private fun loadVacancies() {
        launch {
            updateState { copy(contentState = AppliedVacanciesContentState.Loading) }
            vacancyRepository.getAppliedVacancies()
                .onSuccess { vacancies ->
                    val contentState = if (vacancies.isEmpty()) {
                        AppliedVacanciesContentState.Empty
                    } else {
                        AppliedVacanciesContentState.Data(vacancies.map { it.toCardData() })
                    }
                    updateState { copy(contentState = contentState) }
                }
                .onFailure {
                    updateState {
                        copy(
                            contentState = AppliedVacanciesContentState.Error(
                                R.string.applied_vacancies_error.toTextData()
                            )
                        )
                    }
                }
        }
    }

    private fun Vacancy.toCardData(): VacancyCardData {
        val salaryRange = stringProvider.getString(R.string.vacancy_salary_range, salaryMin, salaryMax)
        val minExperienceYears = if (minExperienceYears == null || minExperienceYears <= 0) {
            stringProvider.getString(R.string.vacancy_no_experience_required)
        } else {
            stringProvider.getString(R.string.vacancy_years_required, minExperienceYears)
        }
        return VacancyCardData(
            id = id,
            title = title.toTextData(),
            companyName = company.name.toTextData(),
            salaryRange = salaryRange.toTextData(),
            requiredExperience = minExperienceYears.toTextData(),
            employmentType = employmentType.toDisplayName(),
            isFavorite = isFavorite
        )
    }
}
