package not.djinni.presentation.screens.public.main

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import not.djinni.presentation.core.components.base.model.VacancyCardData

@Immutable
internal data class PublicMainState(
    val vacanciesListState: PublicVacanciesListState = PublicVacanciesListState.Empty,
)

@Stable
internal sealed class PublicVacanciesListState {
    data object Empty : PublicVacanciesListState()
    data object Loading : PublicVacanciesListState()
    data class Data(val vacancies: List<VacancyCardData>) : PublicVacanciesListState()

    val items: List<VacancyCardData>
        get() = when (this) {
            is Empty -> emptyList()
            is Loading -> emptyList()
            is Data -> vacancies
        }
}
