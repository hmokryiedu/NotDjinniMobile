package not.djinni.presentation.screens.auth.role

internal sealed interface ChooseRoleSideEffect {
    data object NavigateSeekerCreateProfile : ChooseRoleSideEffect
    data object NavigateSeekerMain : ChooseRoleSideEffect
    data object NavigateEmployerCreateProfile : ChooseRoleSideEffect
    data object NavigateEmployerMain : ChooseRoleSideEffect
}
