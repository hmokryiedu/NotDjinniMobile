package not.djinni.presentation.screens.seeker.vacancy.favorite

internal sealed interface FavoriteVacanciesSideEffect {
    data object NavigateBack : FavoriteVacanciesSideEffect
    data class NavigateToVacancyDetails(val vacancyId: Long) : FavoriteVacanciesSideEffect
}
