package not.djinni.presentation.screens.seeker.profile.view

internal sealed interface SeekerProfileAction {
    data object NavigateBack : SeekerProfileAction
    data object ChangeRole : SeekerProfileAction
    data object EditProfile : SeekerProfileAction
    data object RequestLogout : SeekerProfileAction
    data object ConfirmLogout : SeekerProfileAction
    data object DismissLogoutConfirmation : SeekerProfileAction
    data object LoadProfile : SeekerProfileAction
}
