package not.djinni.presentation.screens.employer.profile.my

internal sealed interface EmployerProfileSideEffect {
    data object NavigateBack : EmployerProfileSideEffect
    data object NavigateToAuth : EmployerProfileSideEffect
}
