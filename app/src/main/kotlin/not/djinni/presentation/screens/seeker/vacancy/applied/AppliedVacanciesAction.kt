package not.djinni.presentation.screens.seeker.vacancy.applied

import not.djinni.model.application.ApplicationStatus

internal sealed interface AppliedVacanciesAction {
    data object Load : AppliedVacanciesAction
    data object NavigateBack : AppliedVacanciesAction
    data class OpenVacancy(val vacancyId: Long) : AppliedVacanciesAction
    data object OpenStatusFilter : AppliedVacanciesAction
    data object DismissStatusFilter : AppliedVacanciesAction
    data class ToggleDraftStatus(val status: ApplicationStatus) : AppliedVacanciesAction
    data class RemoveSelectedStatus(val status: ApplicationStatus) : AppliedVacanciesAction
}
