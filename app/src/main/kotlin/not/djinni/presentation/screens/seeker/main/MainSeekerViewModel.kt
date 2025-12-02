@file:OptIn(FlowPreview::class)

package not.djinni.presentation.screens.seeker.main

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import not.djinni.R
import not.djinni.core.extension.mutableSideEffect
import not.djinni.domain.repository.SeekerRepository
import not.djinni.domain.repository.VacancyRepository
import not.djinni.model.seeker.vacancy.Vacancy
import not.djinni.presentation.core.StateViewModel
import not.djinni.presentation.core.components.base.model.VacancyCardData
import not.djinni.presentation.core.extension.toDisplayName
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.screens.seeker.main.model.VacancyTab
import not.djinni.utils.string.StringProvider
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class MainSeekerViewModel(
    private val stringProvider: StringProvider,
    private val seekerRepository: SeekerRepository,
    private val vacancyRepository: VacancyRepository,
) : StateViewModel<MainSeekerState>(
    MainSeekerState()
) {
    private val _sideEffect = mutableSideEffect<MainSeekerSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private val _searchQuery = MutableStateFlow("")

    init {
        collectVacancies()
    }

    fun sendAction(action: MainSeekerAction) {
        when (action) {
            is MainSeekerAction.SelectTab -> handleSelectTab(action.tab)
            is MainSeekerAction.Search -> _searchQuery.tryEmit(action.query.trim())
            is MainSeekerAction.OpenVacancy -> _sideEffect.tryEmit(
                MainSeekerSideEffect.NavigateToVacancyDetails(action.vacancyId)
            )
            MainSeekerAction.OpenProfile -> _sideEffect.tryEmit(
                MainSeekerSideEffect.NavigateToProfile
            )
        }
    }

    private fun handleSelectTab(tab: VacancyTab) {
        updateState { copy(selectedTab = tab) }
    }

    private fun loadVacancies(
        tab: VacancyTab = state.value.selectedTab,
        search: String? = null
    ) {
        launch {
            updateState {
                val isCurrentVacancyListEmpty = vacanciesListState is VacanciesListState.Empty
                copy(vacanciesListState = if (isCurrentVacancyListEmpty) VacanciesListState.Loading else vacanciesListState)
            }
            val state = when (tab) {
                VacancyTab.ALL -> vacancyRepository.getAllVacancies(search)
                VacancyTab.RECOMMENDED -> seekerRepository.getRecommendedVacancies(search)
            }.fold(
                onSuccess = { VacanciesListState.Data(it.map { it.toCardData() }) },
                onFailure = { VacanciesListState.Empty }
            )
            updateState { copy(vacanciesListState = state) }
        }
    }

    private fun collectVacancies() {
        launch {
            combine(
                _searchQuery.debounce(SEARCH_DEBOUNCE_MS),
                state.map { it.selectedTab },
                transform = { query, tab -> query to tab }
            )
                .distinctUntilChanged()
                .collect { (query, tab) ->
                    loadVacancies(tab = tab, search = query.takeIf { it.isNotBlank() })
                }
        }
    }

    private fun Vacancy.toCardData(): VacancyCardData {
        val salaryRange =
            stringProvider.getString(R.string.vacancy_salary_range, salaryMin, salaryMax)
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
            employmentType = employmentType.toDisplayName()
        )
    }

    private companion object {
        const val SEARCH_DEBOUNCE_MS = 200L
    }
}

