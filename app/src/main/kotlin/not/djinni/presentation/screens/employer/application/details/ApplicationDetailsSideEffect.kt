package not.djinni.presentation.screens.employer.application.details

internal sealed interface ApplicationDetailsSideEffect {
    data object NavigateBack : ApplicationDetailsSideEffect
    data class NavigateToVacancyDetails(val vacancyId: Long) : ApplicationDetailsSideEffect
}
