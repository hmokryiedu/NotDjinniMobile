package not.djinni.presentation.screens.onboarding

sealed interface OnboardingAction {
    data class PersonalInformationChanged(
        val fullName: String,
        val aboutMe: String
    ) : OnboardingAction

    data object Continue : OnboardingAction
}
