package not.djinni.presentation.screens.auth

internal sealed interface AuthSideEffect {

    data object NavigateHome : AuthSideEffect
}