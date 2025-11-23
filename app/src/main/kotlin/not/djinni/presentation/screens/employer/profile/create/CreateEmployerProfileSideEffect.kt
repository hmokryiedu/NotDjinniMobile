package not.djinni.presentation.screens.employer

internal sealed interface CreateEmployerProfileSideEffect {
    data object NavigateToHome : CreateEmployerProfileSideEffect
}
