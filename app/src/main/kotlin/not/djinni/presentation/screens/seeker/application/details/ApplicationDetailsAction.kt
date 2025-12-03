package not.djinni.presentation.screens.seeker.application.details

internal sealed interface ApplicationDetailsAction {
    data object NavigateBack : ApplicationDetailsAction
}
