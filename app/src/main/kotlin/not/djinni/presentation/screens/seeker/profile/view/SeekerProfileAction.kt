package not.djinni.presentation.screens.seeker.profile.view

internal sealed interface SeekerProfileAction {
    data object NavigateBack : SeekerProfileAction
    data object ChangeRole : SeekerProfileAction
    data object EditProfile : SeekerProfileAction
    data object Logout : SeekerProfileAction
    data object Retry : SeekerProfileAction
}
