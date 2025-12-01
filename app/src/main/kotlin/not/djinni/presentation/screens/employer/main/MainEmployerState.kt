package not.djinni.presentation.screens.employer.main

import androidx.compose.runtime.Immutable
import not.djinni.presentation.core.components.base.model.VacancyCardData

@Immutable
internal data class MainEmployerState(
    val vacanciesListState: VacanciesListState = VacanciesListState.Empty,
)

sealed class VacanciesListState {
    data object Empty : VacanciesListState()
    data object Loading : VacanciesListState()
    data class Data(val vacancies: List<VacancyCardData>) : VacanciesListState()

    val items: List<VacancyCardData>
        get() = when (this) {
            is Empty -> emptyList()
            is Loading -> emptyList()
            is Data -> vacancies
        }
}
