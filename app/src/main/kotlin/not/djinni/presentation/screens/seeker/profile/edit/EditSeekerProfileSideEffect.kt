package not.djinni.presentation.screens.seeker.profile.edit

internal sealed interface EditSeekerProfileSideEffect {
    data object NavigateBack : EditSeekerProfileSideEffect
}
