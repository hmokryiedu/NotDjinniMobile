package not.djinni.presentation.screens.employer.vacancy.details

internal sealed interface VacancyDetailsSideEffect {
    data object NavigateBack : VacancyDetailsSideEffect
    data class NavigateToApplications(val vacancyId: Long) : VacancyDetailsSideEffect
}
