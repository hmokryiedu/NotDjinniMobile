package not.djinni.presentation.screens.onboarding.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import not.djinni.R
import not.djinni.presentation.core.components.base.NotDjinniText
import not.djinni.presentation.core.components.base.NotDjinniTextField
import not.djinni.presentation.core.components.base.VerticalSpacer
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.screens.onboarding.OnboardingAction
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
fun PersonalInfoStep(
    modifier: Modifier = Modifier,
    onAction: (OnboardingAction.PersonalInformationChanged) -> Unit,
) {
    val nameFieldState = rememberTextFieldState()
    val aboutMeFieldState = rememberTextFieldState()

    Column(modifier = modifier.fillMaxWidth()) {
        NotDjinniText(
            data = R.string.personal_info_title.toTextData(),
            style = NotDjinniTheme.typography.title1,
            color = NotDjinniTheme.colors.onSurface,
        )
        VerticalSpacer(NotDjinniTheme.offsets.huge)
        NotDjinniText(
            data = R.string.full_name_label.toTextData(),
            style = NotDjinniTheme.typography.body2,
            color = NotDjinniTheme.colors.onSurface,
        )
        VerticalSpacer(NotDjinniTheme.offsets.little)
        NotDjinniTextField(
            modifier = Modifier.fillMaxWidth(),
            state = nameFieldState,
            placeholder = R.string.full_name_placeholder.toTextData(),
        )
        VerticalSpacer(NotDjinniTheme.offsets.medium)
        NotDjinniText(
            data = R.string.about_me_label.toTextData(),
            style = NotDjinniTheme.typography.body2,
            color = NotDjinniTheme.colors.onSurface,
        )
        VerticalSpacer(NotDjinniTheme.offsets.little)
        NotDjinniTextField(
            modifier = Modifier.fillMaxWidth(),
            state = aboutMeFieldState,
            placeholder = R.string.about_me_placeholder.toTextData(),
            lineLimits = TextFieldLineLimits.MultiLine(
                minHeightInLines = ABOUT_ME_MIN_LINES,
                maxHeightInLines = ABOUT_ME_MAX_LINES
            ),
        )
    }

    LaunchedEffect(nameFieldState.text, aboutMeFieldState.text) {
        val action = OnboardingAction.PersonalInformationChanged(
            fullName = nameFieldState.text.toString(),
            aboutMe = aboutMeFieldState.text.toString(),
        )
        onAction(action)
    }
}

@Composable
@Preview
private fun Preview() {
    NotDjinniTheme {
        PersonalInfoStep(
            onAction = {}
        )
    }
}

private const val ABOUT_ME_MIN_LINES = 4
private const val ABOUT_ME_MAX_LINES = 10
