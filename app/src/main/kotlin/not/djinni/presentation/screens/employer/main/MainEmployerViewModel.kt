@file:OptIn(FlowPreview::class)

package not.djinni.presentation.screens.employer.main

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import not.djinni.R
import not.djinni.core.extension.mutableSideEffect
import not.djinni.domain.repository.EmployerRepository
import not.djinni.model.seeker.vacancy.Vacancy
import not.djinni.presentation.core.StateViewModel
import not.djinni.presentation.core.components.base.model.VacancyCardData
import not.djinni.presentation.core.extension.toDisplayName
import not.djinni.presentation.core.extension.toTextData
import not.djinni.utils.string.StringProvider
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class MainEmployerViewModel(
    private val stringProvider: StringProvider,
    private val employerRepository: EmployerRepository,
) : StateViewModel<MainEmployerState>(MainEmployerState()) {

    private val _sideEffect = mutableSideEffect<MainEmployerSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()
    private val _searchQuery = MutableStateFlow("")

    init {
        collectVacancies()
    }

    fun sendAction(action: MainEmployerAction) {
        when (action) {
            is MainEmployerAction.LoadData -> loadVacancies(search = state.value.searchQuery.takeIf { it.isNotBlank() })
            is MainEmployerAction.Search -> {
                val query = action.query.trim()
                updateState { copy(searchQuery = query) }
                _searchQuery.tryEmit(query)
            }
            is MainEmployerAction.OpenVacancy -> {
                _sideEffect.tryEmit(MainEmployerSideEffect.NavigateToVacancyDetails(action.vacancyId))
            }

            MainEmployerAction.OpenProfile -> {
                _sideEffect.tryEmit(MainEmployerSideEffect.NavigateToProfile)
            }

            MainEmployerAction.CreateVacancy -> {
                _sideEffect.tryEmit(MainEmployerSideEffect.NavigateToCreateVacancy)
            }
        }
    }

    private fun collectVacancies() {
        launch {
            _searchQuery
                .debounce(SEARCH_DEBOUNCE_MS)
                .distinctUntilChanged()
                .collect { query ->
                    loadVacancies(search = query.takeIf { it.isNotBlank() })
                }
        }
    }

    private fun loadVacancies(search: String? = null) {
        launch {
            updateState {
                val isCurrentVacancyListEmpty = vacanciesListState is VacanciesListState.Empty
                copy(vacanciesListState = if (isCurrentVacancyListEmpty) VacanciesListState.Loading else vacanciesListState)
            }
            val state = employerRepository.getEmployerVacancies(search = search).fold(
                onSuccess = { VacanciesListState.Data(it.map { vacancy -> vacancy.toCardData() }) },
                onFailure = { VacanciesListState.Empty }
            )
            updateState { copy(vacanciesListState = state) }
        }
    }

    private fun Vacancy.toCardData(): VacancyCardData {
        val salaryRange =
            stringProvider.getString(R.string.vacancy_salary_range, salaryMin, salaryMax)
        val experienceText = if (minExperienceYears == null || minExperienceYears <= 0) {
            stringProvider.getString(R.string.vacancy_no_experience_required)
        } else {
            stringProvider.getString(R.string.vacancy_years_required, minExperienceYears)
        }
        return VacancyCardData(
            id = id,
            title = title.toTextData(),
            companyName = company.name.toTextData(),
            salaryRange = salaryRange.toTextData(),
            requiredExperience = experienceText.toTextData(),
            employmentType = employmentType.toDisplayName(),
            isFavorite = isFavorite
        )
    }

    private companion object {
        const val SEARCH_DEBOUNCE_MS = 200L
    }
}
