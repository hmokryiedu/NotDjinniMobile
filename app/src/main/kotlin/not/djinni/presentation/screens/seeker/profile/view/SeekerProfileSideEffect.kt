package not.djinni.presentation.screens.seeker.profile.view

internal sealed interface SeekerProfileSideEffect {
    data object NavigateToChooseRole : SeekerProfileSideEffect
}
