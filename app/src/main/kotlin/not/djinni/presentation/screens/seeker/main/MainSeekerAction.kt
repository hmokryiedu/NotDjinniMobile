package not.djinni.presentation.screens.seeker.main

import not.djinni.presentation.screens.seeker.main.model.VacancyTab

internal sealed interface MainSeekerAction {
    data class SelectTab(val tab: VacancyTab) : MainSeekerAction
    data class Search(val query: String) : MainSeekerAction
    data class OpenVacancy(val vacancyId: Long) : MainSeekerAction
    data class ToggleFavorite(val vacancyId: Long, val isFavorite: Boolean) : MainSeekerAction
    data object OpenProfile : MainSeekerAction
    data object OpenApplications : MainSeekerAction
    data object OpenFavoriteVacancies : MainSeekerAction
}
