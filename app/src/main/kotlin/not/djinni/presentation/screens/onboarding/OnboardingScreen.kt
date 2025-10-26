package not.djinni.presentation.screens.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import not.djinni.R
import not.djinni.presentation.core.Screen
import not.djinni.presentation.core.components.base.FullscreenColumn
import not.djinni.presentation.core.components.base.NotDjinniButton
import not.djinni.presentation.core.components.base.buildFullscreenColumnPadding
import not.djinni.presentation.core.components.base.model.ButtonData
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.screens.onboarding.components.CompletedStep
import not.djinni.presentation.screens.onboarding.components.PersonalInfoStep
import not.djinni.presentation.screens.onboarding.components.WorkPreferencesStep
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
fun OnboardingScreen() {
    Screen<OnboardingViewModel> { viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()

        Content(
            state = state,
            onAction = {},
        )
    }
}

@Composable
private fun Content(
    state: OnboardingState,
    onAction: (OnboardingAction) -> Unit,
) {
    FullscreenColumn(
        contentPadding = buildFullscreenColumnPadding(
            vertical = NotDjinniTheme.offsets.medium,
            horizontal = NotDjinniTheme.offsets.large,
        ),
    ) {
        AnimatedContent(
            targetState = state.step,
            transitionSpec = {
                slideInHorizontally { fullWidth -> fullWidth } + fadeIn() togetherWith
                        slideOutHorizontally { fullWidth -> -fullWidth } + fadeOut()
            },
            label = "OnboardingStepAnimation"
        ) { step ->
            when (step) {
                Step.PERSONAL_INFORMATION -> PersonalInfoStep()
                Step.JOB_PREFERENCES -> WorkPreferencesStep()
                Step.COMPLETED -> CompletedStep()
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        NotDjinniButton(
            modifier = Modifier.fillMaxWidth(),
            data = ButtonData(
                text = R.string.continue_button.toTextData(),
            ),
            onClick = { onAction(OnboardingAction.Continue) },
        )
    }
}

@Composable
@Preview
private fun Preview() {
    NotDjinniTheme {
        val state = OnboardingState()
        Content(
            state = state,
            onAction = {},
        )
    }
}
