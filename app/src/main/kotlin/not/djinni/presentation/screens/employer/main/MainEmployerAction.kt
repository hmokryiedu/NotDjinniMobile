package not.djinni.presentation.screens.employer

internal sealed interface MainEmployerAction {
    data class OpenVacancy(val vacancyId: Long) : MainEmployerAction
}
