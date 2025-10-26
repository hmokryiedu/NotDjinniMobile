package not.djinni.presentation.screens.onboarding

import androidx.compose.runtime.Immutable
import not.djinni.R
import not.djinni.model.EmploymentType
import not.djinni.model.JobCategory
import not.djinni.model.Location
import not.djinni.presentation.core.components.base.model.ButtonData
import not.djinni.presentation.core.extension.toTextData

@Immutable
data class OnboardingState(
    val step: Step = Step.PERSONAL_INFORMATION,
    val buttonData: ButtonData = ButtonData(
        text = R.string.continue_button.toTextData(),
        enabled = false
    ),
    val jobCategory: JobCategory? = null,
    val employmentType: EmploymentType? = null,
    val location: Location? = null,
    val currentAlert: OnboardingAlert? = null
)

enum class Step {
    PERSONAL_INFORMATION,
    JOB_PREFERENCES,
    COMPLETED
}
