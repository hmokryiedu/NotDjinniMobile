package not.djinni.presentation.screens.employer.main

internal sealed interface MainEmployerAction {
    data class OpenVacancy(val vacancyId: Long) : MainEmployerAction
}
