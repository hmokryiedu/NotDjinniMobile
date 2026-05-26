package not.djinni.presentation.screens.employer.main

internal sealed interface MainEmployerAction {
    data object LoadData : MainEmployerAction
    data class Search(val query: String) : MainEmployerAction
    data class OpenVacancy(val vacancyId: Long) : MainEmployerAction
    data object OpenProfile : MainEmployerAction
    data object CreateVacancy : MainEmployerAction
}
