package not.djinni.presentation.screens.employer.vacancy.details

internal sealed interface VacancyDetailsAction {
    data object Retry : VacancyDetailsAction
    data object NavigateBack : VacancyDetailsAction
    data object ViewApplications : VacancyDetailsAction
}
