package not.djinni.presentation.screens.onboarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import not.djinni.R
import not.djinni.presentation.core.components.base.HorizontalSpacer
import not.djinni.presentation.core.components.base.NotDjinniText
import not.djinni.presentation.core.components.base.NotDjinniTextField
import not.djinni.presentation.core.components.base.VerticalSpacer
import not.djinni.presentation.core.components.base.model.TextData
import not.djinni.presentation.core.extension.clickableNoRipple
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.screens.onboarding.OnboardingAction
import not.djinni.presentation.screens.onboarding.OnboardingState
import not.djinni.presentation.screens.onboarding.mapper.toTextData
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
fun WorkPreferencesStep(
    modifier: Modifier = Modifier,
    state: OnboardingState,
    onAction: (OnboardingAction) -> Unit,
) {
    val yearsOfExperienceFieldState = rememberTextFieldState()
    val salaryExpectationsFieldState = rememberTextFieldState()

    Column(modifier = modifier.fillMaxWidth()) {
        NotDjinniText(
            data = R.string.work_preferences_title.toTextData(),
            style = NotDjinniTheme.typography.title1,
            color = NotDjinniTheme.colors.onSurface,
        )
        VerticalSpacer(NotDjinniTheme.offsets.huge)
        NotDjinniText(
            data = R.string.job_category_label.toTextData(),
            style = NotDjinniTheme.typography.body2,
            color = NotDjinniTheme.colors.onSurface,
        )
        VerticalSpacer(NotDjinniTheme.offsets.little)
        SelectorText(
            placeholder = R.string.job_category_placeholder.toTextData(),
            text = state.jobCategory?.name?.toTextData(),
            onClick = { onAction(OnboardingAction.ShowJobCategoryAlert) }
        )
        VerticalSpacer(NotDjinniTheme.offsets.medium)
        NotDjinniText(
            data = R.string.years_of_experience_label.toTextData(),
            style = NotDjinniTheme.typography.body2,
            color = NotDjinniTheme.colors.onSurface,
        )
        VerticalSpacer(NotDjinniTheme.offsets.little)
        NotDjinniTextField(
            modifier = Modifier.fillMaxWidth(),
            state = yearsOfExperienceFieldState,
            placeholder = R.string.years_of_experience_placeholder.toTextData(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        VerticalSpacer(NotDjinniTheme.offsets.medium)
        NotDjinniText(
            data = R.string.employment_type_label.toTextData(),
            style = NotDjinniTheme.typography.body2,
            color = NotDjinniTheme.colors.onSurface,
        )
        VerticalSpacer(NotDjinniTheme.offsets.little)
        SelectorText(
            placeholder = R.string.employment_type_placeholder.toTextData(),
            text = state.employmentType?.toTextData(),
            onClick = { onAction(OnboardingAction.ShowEmploymentTypeAlert) }
        )
        VerticalSpacer(NotDjinniTheme.offsets.medium)
        NotDjinniText(
            data = R.string.work_location_label.toTextData(),
            style = NotDjinniTheme.typography.body2,
            color = NotDjinniTheme.colors.onSurface,
        )
        VerticalSpacer(NotDjinniTheme.offsets.little)
        SelectorText(
            placeholder = R.string.work_location_placeholder.toTextData(),
            text = state.location?.name?.toTextData(),
            onClick = { onAction(OnboardingAction.ShowLocationAlert) }
        )
        VerticalSpacer(NotDjinniTheme.offsets.medium)
        NotDjinniText(
            data = R.string.salary_expectations_label.toTextData(),
            style = NotDjinniTheme.typography.body2,
            color = NotDjinniTheme.colors.onSurface,
        )
        VerticalSpacer(NotDjinniTheme.offsets.little)
        NotDjinniTextField(
            modifier = Modifier.fillMaxWidth(),
            state = salaryExpectationsFieldState,
            placeholder = R.string.salary_expectations_placeholder.toTextData(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }

    LaunchedEffect(
        state.jobCategory,
        yearsOfExperienceFieldState.text,
        state.employmentType,
        state.location,
        salaryExpectationsFieldState.text
    ) {
        val action = OnboardingAction.WorkPreferencesChanged(
            jobCategory = state.jobCategory?.id.orEmpty(),
            yearsOfExperience = yearsOfExperienceFieldState.text.toString(),
            employmentType = state.employmentType?.name.orEmpty(),
            workLocation = state.location?.id.orEmpty(),
            salaryExpectations = salaryExpectationsFieldState.text.toString(),
        )
        onAction(action)
    }
}

@Composable
private fun SelectorText(
    modifier: Modifier = Modifier,
    placeholder: TextData,
    text: TextData?,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = NotDjinniTheme.colors.onSurface,
                shape = NotDjinniTheme.shapes.small,
            )
            .background(
                color = NotDjinniTheme.colors.primary.copy(alpha = 0.3f),
                shape = NotDjinniTheme.shapes.small,
            )
            .clickableNoRipple(onClick = onClick)
            .padding(
                horizontal = NotDjinniTheme.offsets.medium,
                vertical = NotDjinniTheme.offsets.small,
            ),
        contentAlignment = Alignment.CenterStart,
    ) {
        NotDjinniText(
            data = text ?: placeholder,
            style = NotDjinniTheme.typography.body1.copy(
                color = if (text == null) {
                    NotDjinniTheme.colors.onSurface.copy(alpha = 0.5f)
                } else {
                    NotDjinniTheme.colors.onSurface
                }
            )
        )
    }
}

@Composable
@Preview
private fun Preview() {
    NotDjinniTheme {
        val state = OnboardingState()
        WorkPreferencesStep(
            state = state,
            onAction = {}
        )
    }
}
