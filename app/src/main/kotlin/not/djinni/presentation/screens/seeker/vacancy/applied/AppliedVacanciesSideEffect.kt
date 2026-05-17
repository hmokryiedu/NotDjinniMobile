package not.djinni.presentation.screens.seeker.vacancy.applied

internal sealed interface AppliedVacanciesSideEffect {
    data object NavigateBack : AppliedVacanciesSideEffect
    data class NavigateToVacancyDetails(val vacancyId: Long) : AppliedVacanciesSideEffect
}
