package not.djinni.presentation.screens.seeker.application.details

internal sealed interface ApplicationDetailsSideEffect {
    data object NavigateBack : ApplicationDetailsSideEffect
    data object WithdrawSuccess : ApplicationDetailsSideEffect
}
