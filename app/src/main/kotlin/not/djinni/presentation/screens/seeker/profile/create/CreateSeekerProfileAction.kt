package not.djinni.presentation.screens.seeker.profile.create

internal sealed interface CreateSeekerProfileAction {
    data object InitializeProfile : CreateSeekerProfileAction
}
