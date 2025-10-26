package not.djinni.presentation.screens.onboarding

import androidx.compose.runtime.Immutable

@Immutable
data class OnboardingState(
    val step: Step = Step.PERSONAL_INFORMATION,
)

enum class Step {
    PERSONAL_INFORMATION,
    JOB_PREFERENCES,
    COMPLETED
}
