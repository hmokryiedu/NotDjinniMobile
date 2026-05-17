package not.djinni.presentation.screens.public.main

internal sealed interface PublicMainAction {
    data class Search(val query: String) : PublicMainAction
    data class OpenVacancy(val vacancyId: Long) : PublicMainAction
    data object OpenLogin : PublicMainAction
}
