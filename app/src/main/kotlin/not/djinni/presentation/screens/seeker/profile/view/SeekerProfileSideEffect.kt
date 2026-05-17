package not.djinni.presentation.screens.seeker.profile.view

internal sealed interface SeekerProfileSideEffect {
    data object NavigateBack : SeekerProfileSideEffect
    data object NavigateToChooseRole : SeekerProfileSideEffect
    data object NavigateToAuth : SeekerProfileSideEffect
}
