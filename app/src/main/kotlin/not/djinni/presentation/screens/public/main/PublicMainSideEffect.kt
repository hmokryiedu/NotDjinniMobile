package not.djinni.presentation.screens.public.main

internal sealed interface PublicMainSideEffect {
    data class NavigateToVacancyDetails(val vacancyId: Long) : PublicMainSideEffect
    data object NavigateToLogin : PublicMainSideEffect
}
