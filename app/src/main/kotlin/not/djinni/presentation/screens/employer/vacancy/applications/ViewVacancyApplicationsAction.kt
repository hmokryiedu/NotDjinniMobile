package not.djinni.presentation.screens.employer.vacancy.applications

internal sealed interface ViewVacancyApplicationsAction {
    data object LoadApplications : ViewVacancyApplicationsAction
    data object NavigateBack : ViewVacancyApplicationsAction
    data class OpenApplicationDetails(val applicationId: Long) : ViewVacancyApplicationsAction
}
