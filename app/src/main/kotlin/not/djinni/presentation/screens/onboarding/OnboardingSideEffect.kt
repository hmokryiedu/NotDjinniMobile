package not.djinni.presentation.screens.onboarding

sealed interface OnboardingSideEffect {
    data object NavigateToMain : OnboardingSideEffect
}