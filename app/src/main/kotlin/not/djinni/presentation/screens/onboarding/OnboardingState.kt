package not.djinni.presentation.screens.onboarding

import androidx.compose.runtime.Immutable
import not.djinni.R
import not.djinni.presentation.core.components.base.model.ButtonData
import not.djinni.presentation.core.extension.toTextData

@Immutable
data class OnboardingState(
    val step: Step = Step.PERSONAL_INFORMATION,
    val buttonData: ButtonData = ButtonData(
        text = R.string.continue_button.toTextData(),
        enabled = false
    ),
)

enum class Step {
    PERSONAL_INFORMATION,
    JOB_PREFERENCES,
    COMPLETED
}
