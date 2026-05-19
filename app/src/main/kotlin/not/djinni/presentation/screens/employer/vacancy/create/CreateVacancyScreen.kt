package not.djinni.presentation.screens.employer.vacancy.create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import not.djinni.R
import not.djinni.presentation.core.Screen
import not.djinni.presentation.core.components.base.AlertContainer
import not.djinni.presentation.core.components.base.FullscreenColumn
import not.djinni.presentation.core.components.base.HorizontalSpacer
import not.djinni.presentation.core.components.base.NotDjinniButton
import not.djinni.presentation.core.components.base.NotDjinniText
import not.djinni.presentation.core.components.base.NotDjinniTextField
import not.djinni.presentation.core.components.base.VerticalSpacer
import not.djinni.presentation.core.components.base.model.ButtonData
import not.djinni.presentation.core.components.base.model.TextData
import not.djinni.presentation.core.extension.clickableNoRipple
import not.djinni.presentation.core.extension.collectAsEffect
import not.djinni.presentation.core.extension.toDisplayName
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.screens.employer.vacancy.create.components.EmploymentTypeBottomSheet
import not.djinni.presentation.screens.employer.vacancy.create.components.JobCategoryBottomSheet
import not.djinni.presentation.theme.NotDjinniIcons
import not.djinni.presentation.theme.NotDjinniTheme
import org.koin.core.parameter.parametersOf

@Composable
internal fun CreateVacancyScreen(
    sourceVacancyId: Long?,
    onNavigateBack: () -> Unit,
    onNavigateToDetails: (Long) -> Unit,
) {
    Screen<CreateVacancyViewModel>(
        parameters = { parametersOf(sourceVacancyId) }
    ) { viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()

        Content(
            state = state,
            onAction = viewModel::sendAction
        )

        AlertContainer(state.alert) { alert ->
            when (alert) {
                CreateVacancyAlert.SelectEmploymentType -> {
                    EmploymentTypeBottomSheet(
                        selected = state.selectedEmploymentType,
                        onDismiss = { viewModel.sendAction(CreateVacancyAction.HideAlert) },
                        onSelect = {
                            viewModel.sendAction(
                                CreateVacancyAction.SelectEmploymentType(
                                    it
                                )
                            )
                        }
                    )
                }

                CreateVacancyAlert.SelectCategory -> {
                    JobCategoryBottomSheet(
                        selected = state.selectedCategory,
                        onDismiss = { viewModel.sendAction(CreateVacancyAction.HideAlert) },
                        onSelect = { viewModel.sendAction(CreateVacancyAction.SelectCategory(it)) }
                    )
                }
            }
        }

        viewModel.sideEffect.collectAsEffect { effect ->
            when (effect) {
                CreateVacancySideEffect.NavigateBack -> onNavigateBack()
                is CreateVacancySideEffect.NavigateToDetails -> onNavigateToDetails(effect.vacancyId)
            }
        }
    }
}

@Composable
private fun Content(
    state: CreateVacancyState,
    onAction: (CreateVacancyAction) -> Unit = {},
) {
    key(state.prefillVersion) {
        val titleState = rememberTextFieldState(state.title)
        val descriptionState = rememberTextFieldState(state.description)
        val salaryMinState = rememberTextFieldState(state.salaryMin)
        val salaryMaxState = rememberTextFieldState(state.salaryMax)
        val experienceState = rememberTextFieldState(state.experienceYears)

        FullscreenColumn {
            TopBar(onBack = { onAction(CreateVacancyAction.NavigateBack) })
            Column(
                modifier = Modifier
                    .imePadding()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                VerticalSpacer(NotDjinniTheme.offsets.medium)
                NotDjinniText(
                    data = R.string.create_vacancy_job_title.toTextData(),
                    style = NotDjinniTheme.typography.body3,
                    color = NotDjinniTheme.colors.onBackground
                )
                VerticalSpacer(NotDjinniTheme.offsets.tiny)
                NotDjinniTextField(
                    modifier = Modifier.fillMaxWidth(),
                    state = titleState,
                    placeholder = R.string.create_vacancy_job_title_hint.toTextData()
                )
                VerticalSpacer(NotDjinniTheme.offsets.medium)
                NotDjinniText(
                    data = R.string.create_vacancy_description.toTextData(),
                    style = NotDjinniTheme.typography.body3,
                    color = NotDjinniTheme.colors.onBackground
                )
                VerticalSpacer(NotDjinniTheme.offsets.tiny)
                NotDjinniTextField(
                    modifier = Modifier.fillMaxWidth(),
                    state = descriptionState,
                    lineLimits = TextFieldLineLimits.MultiLine(
                        minHeightInLines = DESCRIPTION_MIN_LINES,
                        maxHeightInLines = DESCRIPTION_MAX_LINES
                    ),
                    placeholder = R.string.create_vacancy_description_hint.toTextData()
                )
                VerticalSpacer(NotDjinniTheme.offsets.medium)
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        NotDjinniText(
                            data = R.string.create_vacancy_salary_min.toTextData(),
                            style = NotDjinniTheme.typography.body3,
                            color = NotDjinniTheme.colors.onBackground
                        )
                        VerticalSpacer(NotDjinniTheme.offsets.tiny)
                        NotDjinniTextField(
                            modifier = Modifier.fillMaxWidth(),
                            state = salaryMinState,
                            outputTransformation = { if (originalText.isNotEmpty()) append("$") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            placeholder = R.string.create_vacancy_salary_min_hint.toTextData()
                        )
                    }
                    HorizontalSpacer(NotDjinniTheme.offsets.medium)
                    Column(modifier = Modifier.weight(1f)) {
                        NotDjinniText(
                            data = R.string.create_vacancy_salary_max.toTextData(),
                            style = NotDjinniTheme.typography.body3,
                            color = NotDjinniTheme.colors.onBackground
                        )
                        VerticalSpacer(NotDjinniTheme.offsets.tiny)
                        NotDjinniTextField(
                            modifier = Modifier.fillMaxWidth(),
                            state = salaryMaxState,
                            outputTransformation = { if (originalText.isNotEmpty()) append("$") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            placeholder = R.string.create_vacancy_salary_max_hint.toTextData()
                        )
                    }
                }
                VerticalSpacer(NotDjinniTheme.offsets.medium)
                NotDjinniText(
                    data = R.string.create_vacancy_experience_years.toTextData(),
                    style = NotDjinniTheme.typography.body3,
                    color = NotDjinniTheme.colors.onBackground
                )
                VerticalSpacer(NotDjinniTheme.offsets.tiny)
                NotDjinniTextField(
                    modifier = Modifier.fillMaxWidth(),
                    state = experienceState,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    placeholder = R.string.create_vacancy_experience_years_hint.toTextData()
                )
                VerticalSpacer(NotDjinniTheme.offsets.medium)
                NotDjinniText(
                    data = R.string.create_vacancy_employment_type.toTextData(),
                    style = NotDjinniTheme.typography.body3,
                    color = NotDjinniTheme.colors.onBackground
                )
                VerticalSpacer(NotDjinniTheme.offsets.tiny)
                SelectableField(
                    value = state.selectedEmploymentType?.toDisplayName(),
                    placeholder = R.string.create_vacancy_employment_type_hint.toTextData(),
                    onClick = { onAction(CreateVacancyAction.ShowEmploymentTypeSheet) }
                )
                VerticalSpacer(NotDjinniTheme.offsets.medium)
                NotDjinniText(
                    data = R.string.create_vacancy_category.toTextData(),
                    style = NotDjinniTheme.typography.body3,
                    color = NotDjinniTheme.colors.onBackground
                )
                VerticalSpacer(NotDjinniTheme.offsets.tiny)
                SelectableField(
                    value = state.selectedCategory?.toDisplayName(),
                    placeholder = R.string.create_vacancy_category_hint.toTextData(),
                    onClick = { onAction(CreateVacancyAction.ShowCategorySheet) }
                )
            }
            VerticalSpacer(NotDjinniTheme.offsets.medium)
            NotDjinniButton(
                modifier = Modifier.fillMaxWidth(),
                data = ButtonData(text = R.string.create_vacancy_submit.toTextData()),
                onClick = {
                    val action = CreateVacancyAction.SubmitVacancy(
                        title = titleState.text.toString(),
                        description = descriptionState.text.toString(),
                        salaryMin = salaryMinState.text.toString(),
                        salaryMax = salaryMaxState.text.toString(),
                        experienceYears = experienceState.text.toString(),
                    )
                    onAction(action)
                }
            )
            VerticalSpacer(NotDjinniTheme.offsets.medium)
        }
    }
}

@Composable
private fun TopBar(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = NotDjinniTheme.offsets.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier
                .size(ICON_SIZE)
                .clickableNoRipple(onClick = onBack),
            imageVector = NotDjinniIcons.back,
            contentDescription = null,
            tint = NotDjinniTheme.colors.onBackground
        )
        HorizontalSpacer(NotDjinniTheme.offsets.small)
        NotDjinniText(
            data = R.string.create_vacancy_title.toTextData(),
            style = NotDjinniTheme.typography.title2.copy(fontWeight = FontWeight.Bold),
            color = NotDjinniTheme.colors.onBackground
        )
    }
}

@Composable
private fun SelectableField(
    value: TextData?,
    placeholder: TextData,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = NotDjinniTheme.colors.primary.copy(alpha = FIELD_BACKGROUND_ALPHA),
                shape = NotDjinniTheme.shapes.small
            )
            .clickableNoRipple(onClick = onClick)
            .padding(
                horizontal = NotDjinniTheme.offsets.medium,
                vertical = NotDjinniTheme.offsets.small
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NotDjinniText(
            modifier = Modifier.weight(1f),
            data = value ?: placeholder,
            style = NotDjinniTheme.typography.body1,
            color = if (value != null) NotDjinniTheme.colors.onSurface
            else NotDjinniTheme.colors.onSurface.copy(alpha = PLACEHOLDER_ALPHA)
        )
        Icon(
            modifier = Modifier.size(ICON_SIZE),
            imageVector = NotDjinniIcons.dropDown,
            contentDescription = null,
            tint = NotDjinniTheme.colors.onSurface
        )
    }
}

@Composable
@Preview
private fun Preview() {
    NotDjinniTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(NotDjinniTheme.colors.background)
        ) {
            Content(state = CreateVacancyState())
        }
    }
}

private const val DESCRIPTION_MIN_LINES = 3
private const val DESCRIPTION_MAX_LINES = 6
private const val FIELD_BACKGROUND_ALPHA = 0.3f
private const val PLACEHOLDER_ALPHA = 0.5f
private val ICON_SIZE = 24.dp
