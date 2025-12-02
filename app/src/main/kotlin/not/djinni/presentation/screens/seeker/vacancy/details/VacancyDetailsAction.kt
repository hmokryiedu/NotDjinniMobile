package not.djinni.presentation.screens.seeker.vacancy.details

internal sealed interface VacancyDetailsAction {
    data object Load : VacancyDetailsAction
    data object Apply : VacancyDetailsAction
    data object NavigateBack : VacancyDetailsAction
}
