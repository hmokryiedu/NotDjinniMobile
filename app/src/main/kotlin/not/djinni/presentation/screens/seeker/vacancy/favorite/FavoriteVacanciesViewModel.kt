package not.djinni.presentation.screens.seeker.vacancy.favorite

import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
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
internal class FavoriteVacanciesViewModel(
    private val stringProvider: StringProvider,
    private val vacancyRepository: VacancyRepository,
) : StateViewModel<FavoriteVacanciesState>(
    FavoriteVacanciesState()
) {

    private val _sideEffect = mutableSideEffect<FavoriteVacanciesSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        observeFavoriteChanges()
        loadVacancies()
    }

    fun sendAction(action: FavoriteVacanciesAction) {
        when (action) {
            FavoriteVacanciesAction.Load -> loadVacancies()
            FavoriteVacanciesAction.NavigateBack -> _sideEffect.tryEmit(FavoriteVacanciesSideEffect.NavigateBack)
            is FavoriteVacanciesAction.OpenVacancy -> _sideEffect.tryEmit(
                FavoriteVacanciesSideEffect.NavigateToVacancyDetails(action.vacancyId)
            )
            is FavoriteVacanciesAction.RemoveFavorite -> removeFavorite(action.vacancyId)
        }
    }

    private fun loadVacancies() {
        launch {
            updateState { copy(contentState = FavoriteVacanciesContentState.Loading) }
            vacancyRepository.getFavoriteVacancies()
                .onSuccess { vacancies ->
                    val contentState = if (vacancies.isEmpty()) {
                        FavoriteVacanciesContentState.Empty
                    } else {
                        FavoriteVacanciesContentState.Data(vacancies.map { it.toCardData() })
                    }
                    updateState { copy(contentState = contentState) }
                }
                .onFailure {
                    updateState {
                        copy(
                            contentState = FavoriteVacanciesContentState.Error(
                                R.string.favorite_vacancies_error.toTextData()
                            )
                        )
                    }
                }
        }
    }

    private fun removeFavorite(vacancyId: Long) {
        launch {
            vacancyRepository.removeFavoriteVacancy(vacancyId)
                .onSuccess { removeVacancyFromState(vacancyId) }
        }
    }

    private fun observeFavoriteChanges() {
        launch {
            vacancyRepository.favoriteVacancyChanges.collect { change ->
                if (!change.isFavorite) removeVacancyFromState(change.vacancyId)
            }
        }
    }

    private fun removeVacancyFromState(vacancyId: Long) {
        updateState {
            val currentContentState = contentState
            if (currentContentState !is FavoriteVacanciesContentState.Data) return@updateState this
            val vacancies = currentContentState.vacancies.filterNot { it.id == vacancyId }
            copy(
                contentState = if (vacancies.isEmpty()) {
                    FavoriteVacanciesContentState.Empty
                } else {
                    currentContentState.copy(vacancies = vacancies)
                }
            )
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
            isFavorite = true
        )
    }
}
