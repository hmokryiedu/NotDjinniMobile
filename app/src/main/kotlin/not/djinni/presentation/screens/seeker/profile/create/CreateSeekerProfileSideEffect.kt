package not.djinni.presentation.screens.seeker.profile.create

internal sealed interface CreateSeekerProfileSideEffect {
    data object NavigateToHome : CreateSeekerProfileSideEffect
}
