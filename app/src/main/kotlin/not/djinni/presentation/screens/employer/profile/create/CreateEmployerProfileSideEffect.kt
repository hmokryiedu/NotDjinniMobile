package not.djinni.presentation.screens.employer.profile.create

internal sealed interface CreateEmployerProfileSideEffect {
    data object NavigateToHome : CreateEmployerProfileSideEffect
}
