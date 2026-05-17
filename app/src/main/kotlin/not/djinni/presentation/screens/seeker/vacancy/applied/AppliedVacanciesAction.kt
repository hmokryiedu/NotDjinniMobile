package not.djinni.presentation.screens.seeker.vacancy.applied

internal sealed interface AppliedVacanciesAction {
    data object Load : AppliedVacanciesAction
    data object NavigateBack : AppliedVacanciesAction
    data class OpenVacancy(val vacancyId: Long) : AppliedVacanciesAction
}
