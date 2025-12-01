package not.djinni.presentation.screens.employer.main

internal sealed interface MainEmployerSideEffect {
    data class NavigateToVacancyDetails(val vacancyId: Long) : MainEmployerSideEffect
}
