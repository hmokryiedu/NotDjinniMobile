@file:OptIn(FlowPreview::class)

package not.djinni.presentation.screens.public.main

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
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
internal class PublicMainViewModel(
    private val stringProvider: StringProvider,
    private val vacancyRepository: VacancyRepository,
) : StateViewModel<PublicMainState>(PublicMainState()) {

    private val _sideEffect = mutableSideEffect<PublicMainSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private val _searchQuery = MutableStateFlow("")

    init {
        collectVacancies()
    }

    fun sendAction(action: PublicMainAction) {
        when (action) {
            is PublicMainAction.Search -> _searchQuery.tryEmit(action.query.trim())
            is PublicMainAction.OpenVacancy -> _sideEffect.tryEmit(
                PublicMainSideEffect.NavigateToVacancyDetails(action.vacancyId)
            )

            PublicMainAction.OpenLogin -> _sideEffect.tryEmit(PublicMainSideEffect.NavigateToLogin)
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
                val isCurrentVacancyListEmpty = vacanciesListState is PublicVacanciesListState.Empty
                copy(
                    vacanciesListState = if (isCurrentVacancyListEmpty) {
                        PublicVacanciesListState.Loading
                    } else {
                        vacanciesListState
                    }
                )
            }
            val state = vacancyRepository.getPublicVacancies(search)
                .fold(
                    onSuccess = { PublicVacanciesListState.Data(it.map { vacancy -> vacancy.toCardData() }) },
                    onFailure = { PublicVacanciesListState.Empty },
                )
            updateState { copy(vacanciesListState = state) }
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
