package not.djinni.presentation.screens.employer

internal sealed interface CreateEmployerProfileAction {
    data object InitializeProfile : CreateEmployerProfileAction
}
