@file:OptIn(ExperimentalLayoutApi::class)

package not.djinni.presentation.screens.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import not.djinni.presentation.core.Screen
import not.djinni.presentation.core.components.base.FullscreenColumn
import not.djinni.presentation.core.components.base.NotDjinniButton
import not.djinni.presentation.core.components.base.buildFullscreenColumnPadding
import not.djinni.presentation.core.extension.collectAsEffect
import not.djinni.presentation.screens.onboarding.components.CompletedStep
import not.djinni.presentation.screens.onboarding.components.EmploymentTypeSelectionAlert
import not.djinni.presentation.screens.onboarding.components.JobCategorySelectionAlert
import not.djinni.presentation.screens.onboarding.components.LocationSelectionAlert
import not.djinni.presentation.screens.onboarding.components.PersonalInfoStep
import not.djinni.presentation.screens.onboarding.components.WorkPreferencesStep
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
fun OnboardingScreen(
    onMain: () -> Unit,
) {
    Screen<OnboardingViewModel> { viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()
        val isImeVisible = WindowInsets.isImeVisible
        val focusManager = LocalFocusManager.current

        Content(
            state = state,
            onAction = viewModel::onAction,
        )

        when (val alert = state.currentAlert) {
            is OnboardingAlert.JobCategorySelection -> {
                JobCategorySelectionAlert(
                    categories = alert.categories,
                    selected = alert.selected,
                    onDismiss = { viewModel.onAction(OnboardingAction.DismissAlert) },
                    onApply = { viewModel.onAction(OnboardingAction.SelectJobCategory(it)) }
                )
            }

            is OnboardingAlert.EmploymentTypeSelection -> {
                EmploymentTypeSelectionAlert(
                    types = alert.types,
                    selected = alert.selected,
                    onDismiss = { viewModel.onAction(OnboardingAction.DismissAlert) },
                    onApply = { viewModel.onAction(OnboardingAction.SelectEmploymentType(it)) }
                )
            }

            is OnboardingAlert.LocationSelection -> {
                LocationSelectionAlert(
                    locations = alert.locations,
                    selected = alert.selected,
                    onDismiss = { viewModel.onAction(OnboardingAction.DismissAlert) },
                    onApply = { viewModel.onAction(OnboardingAction.SelectLocation(it)) }
                )
            }

            else -> Unit
        }

        viewModel.sideEffect.collectAsEffect { effect ->
            when (effect) {
                OnboardingSideEffect.NavigateToMain -> onMain()
            }
        }

        LaunchedEffect(isImeVisible) {
            if (!isImeVisible) focusManager.clearFocus()
        }
    }
}

@Composable
private fun Content(
    state: OnboardingState,
    onAction: (OnboardingAction) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    FullscreenColumn(
        contentPadding = buildFullscreenColumnPadding(
            vertical = NotDjinniTheme.offsets.medium,
            horizontal = NotDjinniTheme.offsets.empty,
        ),
    ) {
        AnimatedContent(
            modifier = Modifier
                .padding(horizontal = NotDjinniTheme.offsets.medium)
                .imePadding()
                .verticalScroll(rememberScrollState()),
            targetState = state.step,
            transitionSpec = {
                slideInHorizontally { fullWidth -> fullWidth } + fadeIn() togetherWith
                        slideOutHorizontally { fullWidth -> -fullWidth } + fadeOut()
            },
            label = "OnboardingStepAnimation"
        ) { step ->
            when (step) {
                Step.PERSONAL_INFORMATION -> PersonalInfoStep(onAction = onAction)
                Step.JOB_PREFERENCES -> WorkPreferencesStep(state = state, onAction = onAction)
                Step.COMPLETED -> CompletedStep()
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        NotDjinniButton(
            modifier = Modifier
                .padding(horizontal = NotDjinniTheme.offsets.medium)
                .fillMaxWidth(),
            data = state.buttonData,
            onClick = {
                keyboardController?.hide()
                onAction(OnboardingAction.Continue)
            },
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
