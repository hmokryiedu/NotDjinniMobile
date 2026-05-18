@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)

package not.djinni.presentation.screens.seeker.profile.create

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import not.djinni.R
import not.djinni.core.extension.toFormatterMonthYearDate
import not.djinni.core.extension.toInstant
import not.djinni.model.seeker.vacancy.JobCategoryCode
import not.djinni.presentation.core.Screen
import not.djinni.presentation.core.components.base.FullscreenColumn
import not.djinni.presentation.core.components.base.HorizontalSpacer
import not.djinni.presentation.core.components.base.MessageCard
import not.djinni.presentation.core.components.base.NotDjinniButton
import not.djinni.presentation.core.components.base.NotDjinniText
import not.djinni.presentation.core.components.base.NotDjinniTextField
import not.djinni.presentation.core.components.base.VerticalSpacer
import not.djinni.presentation.core.components.base.buildDefaultTextFieldDecorator
import not.djinni.presentation.core.components.base.model.ButtonData
import not.djinni.presentation.core.components.base.model.TextData
import not.djinni.presentation.core.extension.clickableNoRipple
import not.djinni.presentation.core.extension.collectAsEffect
import not.djinni.presentation.core.extension.toDisplayName
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.screens.seeker.profile.create.alert.CreateProfileAlert
import not.djinni.presentation.screens.seeker.profile.create.model.WorkExperienceData
import not.djinni.presentation.theme.NotDjinniIcons
import not.djinni.presentation.theme.NotDjinniTheme
import kotlin.time.ExperimentalTime

@Composable
internal fun CreateSeekerProfileScreen(
    onHome: () -> Unit,
) {
    Screen<CreateSeekerProfileViewModel> { viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()

        val specialityFieldState = rememberTextFieldState()
        val desiredSalaryFieldState = rememberTextFieldState()
        val experienceFieldState = rememberTextFieldState()
        val aboutMeFieldState = rememberTextFieldState()

        AnimatedContent(
            targetState = state.currentAlert,
            transitionSpec = { fadeIn() togetherWith fadeOut() }
        ) { currentAlert ->
            when (currentAlert) {
                CreateProfileAlert.ADD_WORK_EXPERIENCE -> {
                    AddWorkExperienceContent(onAction = viewModel::sendAction)
                }

                CreateProfileAlert.SELECT_JOB_CATEGORY -> {
                    SelectJobCategoryContent(
                        selectedCategory = state.selectedJobCategory,
                        onAction = viewModel::sendAction
                    )
                }

                null -> {
                    Content(
                        state = state,
                        specialityFieldState = specialityFieldState,
                        desiredSalaryFieldState = desiredSalaryFieldState,
                        experienceFieldState = experienceFieldState,
                        aboutMeFieldState = aboutMeFieldState,
                        onAction = viewModel::sendAction,
                    )
                }
            }
        }

        MessageCard(
            modifier = Modifier
                .padding(horizontal = NotDjinniTheme.offsets.large)
                .fillMaxWidth()
                .statusBarsPadding(),
            enter = fadeIn() + slideInVertically { -it / 4 },
            exit = fadeOut() + slideOutVertically { -it / 4 },
            message = state.message
        )

        viewModel.sideEffect.collectAsEffect { effect ->
            when (effect) {
                CreateSeekerProfileSideEffect.NavigateToHome -> onHome()
            }
        }

        BackHandler(enabled = state.currentAlert != null) {
            viewModel.sendAction(CreateSeekerProfileAction.HideAlert)
        }
    }
}

@Composable
private fun Content(
    state: CreateSeekerProfileState,
    specialityFieldState: TextFieldState = rememberTextFieldState(),
    desiredSalaryFieldState: TextFieldState = rememberTextFieldState(),
    experienceFieldState: TextFieldState = rememberTextFieldState(),
    aboutMeFieldState: TextFieldState = rememberTextFieldState(),
    onAction: (CreateSeekerProfileAction) -> Unit,
) {
    FullscreenColumn {
        NotDjinniText(
            data = R.string.create_seeker_profile_title.toTextData(),
            style = NotDjinniTheme.typography.title1Bold,
            color = NotDjinniTheme.colors.onBackground,
        )
        VerticalSpacer(NotDjinniTheme.offsets.huge)
        Column(
            modifier = Modifier
                .weight(1f)
                .imePadding()
                .verticalScroll(rememberScrollState())
        ) {
            ProfileDataInput(
                state = specialityFieldState,
                title = R.string.seeker_speciality_title.toTextData(),
                placeholder = R.string.seeker_speciality_hint.toTextData(),
            )
            VerticalSpacer(NotDjinniTheme.offsets.medium)
            Row {
                ProfileDataInput(
                    modifier = Modifier.weight(0.4f),
                    state = experienceFieldState,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    title = R.string.seeker_experience_title.toTextData(),
                    placeholder = R.string.seeker_experience_hint.toTextData(),
                )
                HorizontalSpacer(NotDjinniTheme.offsets.medium)
                ProfileDataInput(
                    modifier = Modifier.weight(0.6f),
                    state = desiredSalaryFieldState,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    outputTransformation = OutputTransformation {
                        if (originalText.isNotBlank()) append("$")
                    },
                    title = R.string.seeker_desired_salary_title.toTextData(),
                    placeholder = R.string.seeker_desired_salary_hint.toTextData(),
                )
            }
            VerticalSpacer(NotDjinniTheme.offsets.medium)
            NotDjinniText(
                data = R.string.seeker_job_category_title.toTextData(),
                style = NotDjinniTheme.typography.body3,
                color = NotDjinniTheme.colors.onBackground,
            )
            VerticalSpacer(NotDjinniTheme.offsets.tiny)
            JobCategorySelectionBox(
                selectedCategory = state.selectedJobCategory,
                onClick = { onAction(CreateSeekerProfileAction.ShowSelectJobCategoryAlert) }
            )
            VerticalSpacer(NotDjinniTheme.offsets.medium)
            ProfileDataInput(
                state = aboutMeFieldState,
                title = R.string.seeker_about_me_title.toTextData(),
                placeholder = R.string.seeker_about_me_hint.toTextData(),
                lineLimits = TextFieldLineLimits.MultiLine(
                    minHeightInLines = 4,
                )
            )
            VerticalSpacer(NotDjinniTheme.offsets.large)
            NotDjinniText(
                data = R.string.seeker_work_experience.toTextData(),
                style = NotDjinniTheme.typography.body3,
                color = NotDjinniTheme.colors.onBackground,
            )
            VerticalSpacer(NotDjinniTheme.offsets.tiny)
            Column(
                verticalArrangement = Arrangement.spacedBy(NotDjinniTheme.offsets.tiny)
            ) {
                state.workExperiences.forEach { workExperience ->
                    WorkExperienceItem(modifier = Modifier.fillMaxWidth(), data = workExperience)
                }
            }
            VerticalSpacer(NotDjinniTheme.offsets.small)
            Box(
                modifier = Modifier
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
                    .clickableNoRipple { onAction(CreateSeekerProfileAction.ShowAddWorkExperienceAlert) }
                    .padding(vertical = NotDjinniTheme.offsets.regular),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = NotDjinniIcons.plus,
                    tint = NotDjinniTheme.colors.onSurface,
                    contentDescription = null,
                )
            }
        }
        VerticalSpacer(NotDjinniTheme.offsets.regular)
        NotDjinniButton(
            modifier = Modifier.fillMaxWidth(),
            data = ButtonData(R.string.create_profile_button.toTextData()),
            onClick = {
                val action = CreateSeekerProfileAction.CreateProfile(
                    speciality = specialityFieldState.text.toString(),
                    desiredSalary = desiredSalaryFieldState.text.toString(),
                    yearsOfExperience = experienceFieldState.text.toString(),
                    aboutMe = aboutMeFieldState.text.toString(),
                )
                onAction(action)
            }
        )
    }
}

@Composable
private fun AddWorkExperienceContent(
    modifier: Modifier = Modifier,
    onAction: (CreateSeekerProfileAction) -> Unit,
) {
    val nameFieldState = rememberTextFieldState()
    val positionFieldState = rememberTextFieldState()
    val descriptionFieldState = rememberTextFieldState()

    val datePickerState = rememberDateRangePickerState(
        initialDisplayMode = DisplayMode.Input,
    )
    var isDatePickerExpanded by remember { mutableStateOf(false) }

    FullscreenColumn(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.clickableNoRipple { onAction(CreateSeekerProfileAction.HideAlert) },
                imageVector = NotDjinniIcons.back,
                tint = NotDjinniTheme.colors.onBackground,
                contentDescription = null,
            )
            HorizontalSpacer(NotDjinniTheme.offsets.small)
            NotDjinniText(
                data = R.string.seeker_add_work_experience.toTextData(),
                color = NotDjinniTheme.colors.onBackground,
                style = NotDjinniTheme.typography.title2
            )
        }
        VerticalSpacer(NotDjinniTheme.offsets.average)
        Column(
            modifier = Modifier
                .imePadding()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            ProfileDataInput(
                title = R.string.seeker_work_experience_company_name_title.toTextData(),
                state = nameFieldState,
                placeholder = R.string.seeker_work_experience_company_name.toTextData(),
            )
            VerticalSpacer(NotDjinniTheme.offsets.medium)
            ProfileDataInput(
                title = R.string.seeker_work_experience_description_title.toTextData(),
                state = descriptionFieldState,
                placeholder = R.string.seeker_work_experience_description.toTextData(),
            )
            VerticalSpacer(NotDjinniTheme.offsets.medium)
            ProfileDataInput(
                title = R.string.seeker_work_experience_position_title.toTextData(),
                state = positionFieldState,
                placeholder = R.string.seeker_work_experience_position.toTextData(),
            )
            VerticalSpacer(NotDjinniTheme.offsets.average)
            NotDjinniText(
                modifier = Modifier.align(Alignment.Start),
                data = R.string.seeker_work_experience_date_range.toTextData(),
                style = NotDjinniTheme.typography.body3,
                color = NotDjinniTheme.colors.onBackground,
            )
            VerticalSpacer(NotDjinniTheme.offsets.tiny)
            NotDjinniText(
                modifier = Modifier
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
                    .clickableNoRipple { isDatePickerExpanded = true }
                    .padding(
                        horizontal = NotDjinniTheme.offsets.medium,
                        vertical = NotDjinniTheme.offsets.small,
                    ),
                data = datePickerState.toFormatterMonthYearDate().toTextData(),
                style = NotDjinniTheme.typography.body1,
                color = NotDjinniTheme.colors.onSurface,
            )
        }
        VerticalSpacer(NotDjinniTheme.offsets.regular)
        NotDjinniButton(
            modifier = Modifier.fillMaxWidth(),
            data = ButtonData(R.string.add.toTextData()),
            onClick = {
                val action = CreateSeekerProfileAction.AddWorkExperience(
                    position = positionFieldState.text.toString(),
                    companyName = nameFieldState.text.toString(),
                    description = descriptionFieldState.text.toString(),
                    startDate = datePickerState.selectedStartDateMillis?.toInstant(),
                    endDate = datePickerState.selectedEndDateMillis?.toInstant(),
                )
                onAction(action)
            }
        )
    }

    if (isDatePickerExpanded) {
        DatePickerAlert(
            state = datePickerState,
            onDismissRequest = { isDatePickerExpanded = false },
        )
    }
}

@Composable
private fun DatePickerAlert(
    modifier: Modifier = Modifier,
    state: DateRangePickerState,
    onDismissRequest: () -> Unit,
) {
    DatePickerDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        confirmButton = {
            NotDjinniButton(
                modifier = Modifier.padding(end = NotDjinniTheme.offsets.small),
                data = ButtonData(R.string.apply.toTextData()),
                onClick = onDismissRequest,
            )
        },
        dismissButton = {
            NotDjinniButton(
                data = ButtonData(R.string.cancel.toTextData()),
                onClick = onDismissRequest,
            )
        }
    ) {
        DateRangePicker(
            dateFormatter = DatePickerDefaults.dateFormatter(
                DatePickerDefaults.YearMonthSkeleton
            ),
            colors = DatePickerDefaults.colors(
                containerColor = NotDjinniTheme.colors.background,
                titleContentColor = NotDjinniTheme.colors.onBackground,
                headlineContentColor = NotDjinniTheme.colors.onBackground,
                weekdayContentColor = NotDjinniTheme.colors.onSurface,
                subheadContentColor = NotDjinniTheme.colors.onSurface,
                dayInSelectionRangeContainerColor = NotDjinniTheme.colors.primary.copy(
                    alpha = 0.1f
                ),
                dayInSelectionRangeContentColor = NotDjinniTheme.colors.onSurface,
                todayDateBorderColor = NotDjinniTheme.colors.onSurface,
                dividerColor = NotDjinniTheme.colors.onSurface.copy(alpha = 0.5f),
                dateTextFieldColors = TextFieldDefaults.colors(
                    focusedIndicatorColor = NotDjinniTheme.colors.onSurface,
                    unfocusedIndicatorColor = NotDjinniTheme.colors.onSurface,
                    focusedLabelColor = NotDjinniTheme.colors.onBackground,
                    unfocusedLabelColor = NotDjinniTheme.colors.onBackground,
                    cursorColor = NotDjinniTheme.colors.primary,
                ),
                selectedDayContainerColor = NotDjinniTheme.colors.primary.copy(
                    0.2f
                ),
                selectedDayContentColor = NotDjinniTheme.colors.onPrimary,
                todayContentColor = NotDjinniTheme.colors.onSurface,
            ),
            title = {
                NotDjinniText(
                    modifier = Modifier.padding(NotDjinniTheme.offsets.small),
                    data = R.string.seeker_work_experience_date_range.toTextData(),
                    style = NotDjinniTheme.typography.title2,
                    color = NotDjinniTheme.colors.onBackground,
                )
            },
            headline = {
                NotDjinniText(
                    modifier = Modifier.padding(NotDjinniTheme.offsets.small),
                    data = state.toFormatterMonthYearDate().toTextData(),
                    style = NotDjinniTheme.typography.body1,
                    color = NotDjinniTheme.colors.onBackground,
                )
            },
            state = state
        )
    }
}

@Composable
private fun WorkExperienceItem(
    modifier: Modifier = Modifier,
    data: WorkExperienceData,
) {
    Column(
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
            .padding(
                horizontal = NotDjinniTheme.offsets.medium,
                vertical = NotDjinniTheme.offsets.small,
            ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                NotDjinniText(
                    data = data.position.toTextData(),
                    style = NotDjinniTheme.typography.body1Bold,
                    color = NotDjinniTheme.colors.onBackground,
                )
                VerticalSpacer(NotDjinniTheme.offsets.tiny)
                NotDjinniText(
                    data = data.companyName.toTextData(),
                    style = NotDjinniTheme.typography.body2,
                    color = NotDjinniTheme.colors.onBackground,
                )
            }
            HorizontalSpacer(NotDjinniTheme.offsets.medium)
            NotDjinniText(
                modifier = Modifier.align(Alignment.Top),
                data = "${data.startDate.toFormatterMonthYearDate()} - ${data.endDate.toFormatterMonthYearDate()}".toTextData(),
                style = NotDjinniTheme.typography.body2,
                color = NotDjinniTheme.colors.onBackground,
            )
        }
        NotDjinniText(
            data = data.description?.toTextData() ?: run {
                R.string.seeker_work_experience_no_description_placeholder.toTextData()
            },
            style = NotDjinniTheme.typography.body3,
            color = NotDjinniTheme.colors.onBackground.copy(
                alpha = if (data.description.isNullOrEmpty()) 0.5f else 1f
            ),
        )
    }
}

@Composable
private fun ProfileDataInput(
    modifier: Modifier = Modifier,
    title: TextData,
    state: TextFieldState,
    placeholder: TextData,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    outputTransformation: OutputTransformation? = null,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.Default,
) {
    Column(modifier = modifier) {
        NotDjinniText(
            data = title,
            style = NotDjinniTheme.typography.body3,
            color = NotDjinniTheme.colors.onBackground,
        )
        VerticalSpacer(NotDjinniTheme.offsets.tiny)
        NotDjinniTextField(
            state = state,
            lineLimits = lineLimits,
            keyboardOptions = keyboardOptions,
            outputTransformation = outputTransformation,
            decorator = buildDefaultTextFieldDecorator(
                state = state,
                placeholder = placeholder,
                textStyle = NotDjinniTheme.typography.body1.copy(
                    color = NotDjinniTheme.colors.onSurface,
                ),
            ),
        )
    }
}

private fun DateRangePickerState.toFormatterMonthYearDate(): String {
    val startDate = this.selectedStartDateMillis.toFormatterMonthYearDate()
    val endDate = this.selectedEndDateMillis.toFormatterMonthYearDate()
    return "$startDate - $endDate"
}

private fun Long?.toFormatterMonthYearDate(): String {
    return this?.toInstant()?.toFormatterMonthYearDate() ?: "..."
}

@Composable
private fun JobCategorySelectionBox(
    modifier: Modifier = Modifier,
    selectedCategory: JobCategoryCode?,
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
    ) {
        NotDjinniText(
            data = selectedCategory?.toDisplayName()
                ?: R.string.seeker_job_category_hint.toTextData(),
            style = NotDjinniTheme.typography.body1,
            color = if (selectedCategory != null) {
                NotDjinniTheme.colors.onSurface
            } else {
                NotDjinniTheme.colors.onSurface.copy(alpha = 0.5f)
            },
        )
    }
}

@Composable
private fun SelectJobCategoryContent(
    modifier: Modifier = Modifier,
    selectedCategory: JobCategoryCode?,
    onAction: (CreateSeekerProfileAction) -> Unit,
) {
    FullscreenColumn(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.clickableNoRipple {
                    onAction(CreateSeekerProfileAction.HideAlert)
                },
                imageVector = NotDjinniIcons.back,
                tint = NotDjinniTheme.colors.onBackground,
                contentDescription = null,
            )
            HorizontalSpacer(NotDjinniTheme.offsets.small)
            NotDjinniText(
                data = R.string.seeker_select_job_category.toTextData(),
                color = NotDjinniTheme.colors.onBackground,
                style = NotDjinniTheme.typography.title2
            )
        }
        VerticalSpacer(NotDjinniTheme.offsets.average)
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(NotDjinniTheme.offsets.tiny)
        ) {
            items(items = JobCategoryCode.entries, key = { it.name }) { category ->
                JobCategoryItem(
                    modifier = Modifier.animateItem(),
                    category = category,
                    isSelected = category == selectedCategory,
                    onClick = { onAction(CreateSeekerProfileAction.SelectJobCategory(category)) }
                )
            }
        }
    }
}

@Composable
private fun JobCategoryItem(
    modifier: Modifier = Modifier,
    category: JobCategoryCode,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = if (isSelected) {
                    NotDjinniTheme.colors.primary
                } else {
                    NotDjinniTheme.colors.onSurface
                },
                shape = NotDjinniTheme.shapes.small,
            )
            .background(
                color = if (isSelected) {
                    NotDjinniTheme.colors.primary.copy(alpha = 0.5f)
                } else {
                    NotDjinniTheme.colors.primary.copy(alpha = 0.3f)
                },
                shape = NotDjinniTheme.shapes.small,
            )
            .clickableNoRipple(onClick = onClick)
            .padding(
                horizontal = NotDjinniTheme.offsets.medium,
                vertical = NotDjinniTheme.offsets.small,
            ),
    ) {
        NotDjinniText(
            data = category.toDisplayName(),
            style = NotDjinniTheme.typography.body1,
            color = NotDjinniTheme.colors.onBackground,
        )
    }
}

@Composable
@Preview
private fun Preview() {
    NotDjinniTheme {
        val state = CreateSeekerProfileState()
        Content(
            state = state,
            onAction = {}
        )
    }
}
