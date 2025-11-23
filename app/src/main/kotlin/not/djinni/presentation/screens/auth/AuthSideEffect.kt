package not.djinni.presentation.screens.auth

internal sealed interface AuthSideEffect {
    data object NavigateNext : AuthSideEffect
}