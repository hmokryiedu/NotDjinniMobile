package not.djinni.presentation.screens.onboarding

sealed interface OnboardingAction {
    data object Continue : OnboardingAction
}
