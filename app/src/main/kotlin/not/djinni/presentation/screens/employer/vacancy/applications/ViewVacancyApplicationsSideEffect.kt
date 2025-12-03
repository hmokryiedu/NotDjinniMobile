package not.djinni.presentation.screens.employer.vacancy.applications

internal sealed interface ViewVacancyApplicationsSideEffect {
    data object NavigateBack : ViewVacancyApplicationsSideEffect
    data class NavigateToApplicationDetails(val applicationId: Long) : ViewVacancyApplicationsSideEffect
}
