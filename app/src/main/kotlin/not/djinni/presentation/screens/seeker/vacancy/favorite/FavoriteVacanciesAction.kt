package not.djinni.presentation.screens.seeker.vacancy.favorite

internal sealed interface FavoriteVacanciesAction {
    data object Load : FavoriteVacanciesAction
    data object NavigateBack : FavoriteVacanciesAction
    data class OpenVacancy(val vacancyId: Long) : FavoriteVacanciesAction
    data class RemoveFavorite(val vacancyId: Long) : FavoriteVacanciesAction
}
