package not.djinni.presentation.screens.seeker.main

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import not.djinni.presentation.core.components.base.model.VacancyCardData
import not.djinni.presentation.screens.seeker.main.model.VacancyTab

@Immutable
internal data class MainSeekerState(
    val selectedTab: VacancyTab = VacancyTab.ALL,
    val vacanciesListState: VacanciesListState = VacanciesListState.Empty,
)

@Stable
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
