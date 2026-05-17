package not.djinni.presentation.screens.public.vacancy.details

internal sealed interface PublicVacancyDetailsAction {
    data object Load : PublicVacancyDetailsAction
    data object NavigateBack : PublicVacancyDetailsAction
}
