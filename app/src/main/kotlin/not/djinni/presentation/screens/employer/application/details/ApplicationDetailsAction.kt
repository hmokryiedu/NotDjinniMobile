package not.djinni.presentation.screens.employer.application.details

import not.djinni.model.application.ApplicationStatus

internal sealed interface ApplicationDetailsAction {
    data object NavigateBack : ApplicationDetailsAction
    data object Retry : ApplicationDetailsAction
    data object ViewVacancyDetails : ApplicationDetailsAction
    data object OpenUpdateStatusSheet : ApplicationDetailsAction
    data object DismissAlert : ApplicationDetailsAction
    data class UpdateStatus(val newStatus: ApplicationStatus) : ApplicationDetailsAction
}
