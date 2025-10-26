package not.djinni.presentation.screens.auth

internal sealed interface AuthSideEffect {
    data object NavigateToMain : AuthSideEffect
    data object NavigateToOnboarding : AuthSideEffect
}