package not.djinni.presentation.screens.seeker.main

internal sealed interface MainSeekerSideEffect {
    data class NavigateToVacancyDetails(val vacancyId: Long) : MainSeekerSideEffect
}
