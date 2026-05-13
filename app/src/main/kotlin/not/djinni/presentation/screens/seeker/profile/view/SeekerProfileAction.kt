package not.djinni.presentation.screens.seeker.profile.view

internal sealed interface SeekerProfileAction {
    data object ChangeRole : SeekerProfileAction
    data object Logout : SeekerProfileAction
    data object Retry : SeekerProfileAction
}
