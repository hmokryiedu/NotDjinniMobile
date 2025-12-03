package not.djinni.presentation.screens.seeker.application.list

internal sealed interface ViewApplicationsAction {
    data object LoadApplications : ViewApplicationsAction
    data class OpenApplicationDetails(val id: Long) : ViewApplicationsAction
}
