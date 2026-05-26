@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)

package not.djinni.presentation.screens.seeker.profile.edit

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
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
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
import not.djinni.presentation.core.components.base.NotDjinniLoader
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
import not.djinni.presentation.screens.seeker.profile.edit.alert.EditProfileAlert
import not.djinni.presentation.theme.NotDjinniIcons
import not.djinni.presentation.theme.NotDjinniTheme
import kotlin.time.ExperimentalTime

@Composable
internal fun EditSeekerProfileScreen(
    onBack: () -> Unit,
) {
    Screen<EditSeekerProfileViewModel> { viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()

        val specialityFieldState = rememberTextFieldState()
        val desiredSalaryFieldState = rememberTextFieldState()
        val experienceFieldState = rememberTextFieldState()
        val aboutMeFieldState = rememberTextFieldState()

        BindProfileFields(
            state = state,
            specialityFieldState = specialityFieldState,
            desiredSalaryFieldState = desiredSalaryFieldState,
            experienceFieldState = experienceFieldState,
            aboutMeFieldState = aboutMeFieldState,
            onAction = viewModel::sendAction,
        )

        AnimatedContent(
            targetState = state.currentAlert,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
        ) { currentAlert ->
            when (currentAlert) {
                EditProfileAlert.ADD_WORK_EXPERIENCE,
                EditProfileAlert.EDIT_WORK_EXPERIENCE -> AddOrEditWorkExperienceContent(
                    item = state.editingWorkExperience,
                    onAction = viewModel::sendAction,
                )

                EditProfileAlert.SELECT_JOB_CATEGORY -> SelectJobCategoryContent(
                    selectedCategory = state.jobCategory,
                    onAction = viewModel::sendAction,
                )

                null -> Content(
                    state = state,
                    specialityFieldState = specialityFieldState,
                    desiredSalaryFieldState = desiredSalaryFieldState,
                    experienceFieldState = experienceFieldState,
                    aboutMeFieldState = aboutMeFieldState,
                    onAction = viewModel::sendAction,
                )
            }
        }

        MessageCard(
            modifier = Modifier
                .padding(horizontal = NotDjinniTheme.offsets.large)
                .fillMaxWidth()
                .statusBarsPadding(),
            enter = fadeIn() + slideInVertically { -it / 4 },
            exit = fadeOut() + slideOutVertically { -it / 4 },
            message = state.message,
        )

        viewModel.sideEffect.collectAsEffect { onBack() }

        BackHandler(enabled = state.currentAlert != null) {
            viewModel.sendAction(EditSeekerProfileAction.HideAlert)
        }
    }
}

@Composable
private fun BindProfileFields(
    state: EditSeekerProfileState,
    specialityFieldState: TextFieldState,
    desiredSalaryFieldState: TextFieldState,
    experienceFieldState: TextFieldState,
    aboutMeFieldState: TextFieldState,
    onAction: (EditSeekerProfileAction) -> Unit,
) {
    LaunchedEffect(state.speciality) {
        if (specialityFieldState.text.toString() != state.speciality) {
            specialityFieldState.setTextAndPlaceCursorAtEnd(state.speciality)
        }
    }
    LaunchedEffect(state.desiredSalary) {
        if (desiredSalaryFieldState.text.toString() != state.desiredSalary) {
            desiredSalaryFieldState.setTextAndPlaceCursorAtEnd(state.desiredSalary)
        }
    }
    LaunchedEffect(state.experienceYears) {
        if (experienceFieldState.text.toString() != state.experienceYears) {
            experienceFieldState.setTextAndPlaceCursorAtEnd(state.experienceYears)
        }
    }
    LaunchedEffect(state.aboutMe) {
        if (aboutMeFieldState.text.toString() != state.aboutMe) {
            aboutMeFieldState.setTextAndPlaceCursorAtEnd(state.aboutMe)
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { specialityFieldState.text.toString() }
            .collect { onAction(EditSeekerProfileAction.UpdateSpeciality(it)) }
    }
    LaunchedEffect(Unit) {
        snapshotFlow { desiredSalaryFieldState.text.toString() }
            .collect { onAction(EditSeekerProfileAction.UpdateDesiredSalary(it)) }
    }
    LaunchedEffect(Unit) {
        snapshotFlow { experienceFieldState.text.toString() }
            .collect { onAction(EditSeekerProfileAction.UpdateExperienceYears(it)) }
    }
    LaunchedEffect(Unit) {
        snapshotFlow { aboutMeFieldState.text.toString() }
            .collect { onAction(EditSeekerProfileAction.UpdateAboutMe(it)) }
    }
}

@Composable
private fun Content(
    state: EditSeekerProfileState,
    specialityFieldState: TextFieldState,
    desiredSalaryFieldState: TextFieldState,
    experienceFieldState: TextFieldState,
    aboutMeFieldState: TextFieldState,
    onAction: (EditSeekerProfileAction) -> Unit,
) {
    FullscreenColumn(horizontalAlignment = Alignment.Start) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.clickableNoRipple { onAction(EditSeekerProfileAction.NavigateBack) },
                imageVector = NotDjinniIcons.back,
                contentDescription = null,
                tint = NotDjinniTheme.colors.onBackground,
            )
            HorizontalSpacer(NotDjinniTheme.offsets.small)
            NotDjinniText(
                data = R.string.edit.toTextData(),
                style = NotDjinniTheme.typography.title1Bold,
                color = NotDjinniTheme.colors.onBackground,
            )
        }
        VerticalSpacer(NotDjinniTheme.offsets.huge)
        when {
            state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { NotDjinniLoader() }
            state.hasError -> {
                NotDjinniButton(
                    modifier = Modifier.fillMaxWidth(),
                    data = ButtonData(R.string.retry.toTextData()),
                    onClick = { onAction(EditSeekerProfileAction.Retry) },
                )
            }

            else -> {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .imePadding()
                        .verticalScroll(rememberScrollState()),
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
                        selectedCategory = state.jobCategory,
                        onClick = { onAction(EditSeekerProfileAction.ShowSelectJobCategoryAlert) },
                    )
                    VerticalSpacer(NotDjinniTheme.offsets.medium)
                    ProfileDataInput(
                        state = aboutMeFieldState,
                        title = R.string.seeker_about_me_title.toTextData(),
                        placeholder = R.string.seeker_about_me_hint.toTextData(),
                        lineLimits = TextFieldLineLimits.MultiLine(minHeightInLines = 4),
                    )
                    VerticalSpacer(NotDjinniTheme.offsets.large)
                    NotDjinniText(
                        data = R.string.seeker_work_experience.toTextData(),
                        style = NotDjinniTheme.typography.body3,
                        color = NotDjinniTheme.colors.onBackground,
                    )
                    VerticalSpacer(NotDjinniTheme.offsets.tiny)
                    Column(verticalArrangement = Arrangement.spacedBy(NotDjinniTheme.offsets.tiny)) {
                        state.workExperiences.forEach { item ->
                            EditWorkExperienceItem(
                                data = item,
                                onEdit = { onAction(EditSeekerProfileAction.ShowEditWorkExperience(item.localKey)) },
                                onDelete = { onAction(EditSeekerProfileAction.RemoveWorkExperience(item.localKey)) },
                            )
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
                            .clickableNoRipple { onAction(EditSeekerProfileAction.ShowAddWorkExperience) }
                            .padding(vertical = NotDjinniTheme.offsets.regular),
                        contentAlignment = Alignment.Center,
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
                    data = ButtonData(
                        if (state.isSaving) "Saving...".toTextData() else R.string.save.toTextData()
                    ),
                    onClick = { onAction(EditSeekerProfileAction.Save) },
                )
            }
        }
    }
}

@Composable
private fun AddOrEditWorkExperienceContent(
    item: EditableWorkExperience?,
    onAction: (EditSeekerProfileAction) -> Unit,
) {
    if (item == null) return

    val companyFieldState = rememberTextFieldState()
    val descriptionFieldState = rememberTextFieldState()
    val positionFieldState = rememberTextFieldState()
    val datePickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = item.startDate?.toEpochMilliseconds(),
        initialSelectedEndDateMillis = item.endDate?.toEpochMilliseconds(),
        initialDisplayMode = DisplayMode.Input,
    )
    var isDatePickerExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(item.localKey) {
        companyFieldState.setTextAndPlaceCursorAtEnd(item.companyName)
        descriptionFieldState.setTextAndPlaceCursorAtEnd(item.description.orEmpty())
        positionFieldState.setTextAndPlaceCursorAtEnd(item.position)
    }
    LaunchedEffect(Unit) {
        snapshotFlow { companyFieldState.text.toString() }
            .collect { onAction(EditSeekerProfileAction.UpdateEditingCompany(it)) }
    }
    LaunchedEffect(Unit) {
        snapshotFlow { descriptionFieldState.text.toString() }
            .collect { onAction(EditSeekerProfileAction.UpdateEditingDescription(it)) }
    }
    LaunchedEffect(Unit) {
        snapshotFlow { positionFieldState.text.toString() }
            .collect { onAction(EditSeekerProfileAction.UpdateEditingPosition(it)) }
    }

    FullscreenColumn(horizontalAlignment = Alignment.Start) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.clickableNoRipple { onAction(EditSeekerProfileAction.HideAlert) },
                imageVector = NotDjinniIcons.back,
                tint = NotDjinniTheme.colors.onBackground,
                contentDescription = null,
            )
            HorizontalSpacer(NotDjinniTheme.offsets.small)
            NotDjinniText(
                data = R.string.seeker_add_work_experience.toTextData(),
                color = NotDjinniTheme.colors.onBackground,
                style = NotDjinniTheme.typography.title2,
            )
        }
        VerticalSpacer(NotDjinniTheme.offsets.average)
        Column(
            modifier = Modifier
                .imePadding()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
        ) {
            ProfileDataInput(
                title = R.string.seeker_work_experience_company_name_title.toTextData(),
                state = companyFieldState,
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
            VerticalSpacer(NotDjinniTheme.offsets.medium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = item.isCurrent,
                    onCheckedChange = { checked ->
                        onAction(EditSeekerProfileAction.UpdateEditingIsCurrent(checked))
                        onAction(
                            EditSeekerProfileAction.UpdateEditingEndDate(
                                if (checked) null else datePickerState.selectedEndDateMillis?.toInstant(),
                            ),
                        )
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = NotDjinniTheme.colors.primary,
                        checkmarkColor = NotDjinniTheme.colors.onPrimary,
                        uncheckedColor = NotDjinniTheme.colors.onSurface,
                    ),
                )
                HorizontalSpacer(NotDjinniTheme.offsets.tiny)
                NotDjinniText(
                    data = "Current position".toTextData(),
                    style = NotDjinniTheme.typography.body2,
                    color = NotDjinniTheme.colors.onBackground,
                )
            }
        }
        VerticalSpacer(NotDjinniTheme.offsets.regular)
        NotDjinniButton(
            modifier = Modifier.fillMaxWidth(),
            data = ButtonData(R.string.save.toTextData()),
            onClick = {
                onAction(EditSeekerProfileAction.UpdateEditingStartDate(datePickerState.selectedStartDateMillis?.toInstant()))
                onAction(
                    EditSeekerProfileAction.UpdateEditingEndDate(
                        if (item.isCurrent) null else datePickerState.selectedEndDateMillis?.toInstant(),
                    ),
                )
                onAction(EditSeekerProfileAction.SaveWorkExperience)
            },
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
private fun EditWorkExperienceItem(
    data: EditableWorkExperience,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    Column(
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
            .padding(
                horizontal = NotDjinniTheme.offsets.medium,
                vertical = NotDjinniTheme.offsets.small,
            ),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
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
            NotDjinniText(
                data = "${data.startDate?.toFormatterMonthYearDate() ?: "..."} - ${if (data.isCurrent || data.endDate == null) "Present" else data.endDate.toFormatterMonthYearDate()}".toTextData(),
                style = NotDjinniTheme.typography.body2,
                color = NotDjinniTheme.colors.onBackground,
            )
        }
        NotDjinniText(
            data = data.description?.toTextData()
                ?: R.string.seeker_work_experience_no_description_placeholder.toTextData(),
            style = NotDjinniTheme.typography.body3,
            color = NotDjinniTheme.colors.onBackground.copy(
                alpha = if (data.description.isNullOrEmpty()) 0.5f else 1f,
            ),
        )
        VerticalSpacer(NotDjinniTheme.offsets.small)
        Row(horizontalArrangement = Arrangement.spacedBy(NotDjinniTheme.offsets.small)) {
            NotDjinniButton(data = ButtonData(R.string.edit.toTextData()), onClick = onEdit)
            NotDjinniButton(data = ButtonData(R.string.delete.toTextData()), onClick = onDelete)
        }
    }
}

@Composable
private fun DatePickerAlert(
    state: DateRangePickerState,
    onDismissRequest: () -> Unit,
) {
    val datePickerColors = DatePickerDefaults.colors(
        containerColor = NotDjinniTheme.colors.background,
        titleContentColor = NotDjinniTheme.colors.onBackground,
        headlineContentColor = NotDjinniTheme.colors.onBackground,
        weekdayContentColor = NotDjinniTheme.colors.onSurface,
        subheadContentColor = NotDjinniTheme.colors.onSurface,
        navigationContentColor = NotDjinniTheme.colors.onSurface,
        yearContentColor = NotDjinniTheme.colors.onSurface,
        currentYearContentColor = NotDjinniTheme.colors.onSurface,
        selectedYearContainerColor = NotDjinniTheme.colors.primary,
        selectedYearContentColor = NotDjinniTheme.colors.onPrimary,
        dayContentColor = NotDjinniTheme.colors.onSurface,
        disabledDayContentColor = NotDjinniTheme.colors.onSurface.copy(alpha = 0.38f),
        selectedDayContainerColor = NotDjinniTheme.colors.primary,
        selectedDayContentColor = NotDjinniTheme.colors.onPrimary,
        todayContentColor = NotDjinniTheme.colors.onSurface,
        todayDateBorderColor = NotDjinniTheme.colors.onSurface,
        dayInSelectionRangeContainerColor = NotDjinniTheme.colors.primary.copy(alpha = 0.18f),
        dayInSelectionRangeContentColor = NotDjinniTheme.colors.onSurface,
        dividerColor = NotDjinniTheme.colors.onSurface,
        dateTextFieldColors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = NotDjinniTheme.colors.onSurface,
            unfocusedTextColor = NotDjinniTheme.colors.onSurface,
            disabledTextColor = NotDjinniTheme.colors.onSurface.copy(alpha = 0.38f),
            focusedBorderColor = NotDjinniTheme.colors.onSurface,
            unfocusedBorderColor = NotDjinniTheme.colors.onSurface,
            disabledBorderColor = NotDjinniTheme.colors.onSurface.copy(alpha = 0.38f),
            focusedLabelColor = NotDjinniTheme.colors.onBackground,
            unfocusedLabelColor = NotDjinniTheme.colors.onBackground,
            disabledLabelColor = NotDjinniTheme.colors.onSurface.copy(alpha = 0.38f),
            cursorColor = NotDjinniTheme.colors.primary,
            errorTextColor = NotDjinniTheme.colors.error,
            errorBorderColor = NotDjinniTheme.colors.error,
            errorLabelColor = NotDjinniTheme.colors.error,
            errorSupportingTextColor = NotDjinniTheme.colors.error,
            errorCursorColor = NotDjinniTheme.colors.error,
        ),
    )

    DatePickerDialog(
        colors = datePickerColors,
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
        },
    ) {
        DateRangePicker(
            dateFormatter = DatePickerDefaults.dateFormatter(DatePickerDefaults.YearMonthSkeleton),
            colors = datePickerColors,
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
            state = state,
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
                textStyle = NotDjinniTheme.typography.body1.copy(color = NotDjinniTheme.colors.onSurface),
            ),
        )
    }
}

@Composable
private fun JobCategorySelectionBox(
    selectedCategory: JobCategoryCode?,
    onClick: () -> Unit,
) {
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
            .clickableNoRipple(onClick = onClick)
            .padding(
                horizontal = NotDjinniTheme.offsets.medium,
                vertical = NotDjinniTheme.offsets.small,
            ),
    ) {
        NotDjinniText(
            data = selectedCategory?.toDisplayName() ?: R.string.seeker_job_category_hint.toTextData(),
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
    selectedCategory: JobCategoryCode?,
    onAction: (EditSeekerProfileAction) -> Unit,
) {
    FullscreenColumn(horizontalAlignment = Alignment.Start) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.clickableNoRipple { onAction(EditSeekerProfileAction.HideAlert) },
                imageVector = NotDjinniIcons.back,
                tint = NotDjinniTheme.colors.onBackground,
                contentDescription = null,
            )
            HorizontalSpacer(NotDjinniTheme.offsets.small)
            NotDjinniText(
                data = R.string.seeker_select_job_category.toTextData(),
                color = NotDjinniTheme.colors.onBackground,
                style = NotDjinniTheme.typography.title2,
            )
        }
        VerticalSpacer(NotDjinniTheme.offsets.average)
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(NotDjinniTheme.offsets.tiny),
        ) {
            items(items = JobCategoryCode.entries, key = { it.name }) { category ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = if (category == selectedCategory) {
                                NotDjinniTheme.colors.primary
                            } else {
                                NotDjinniTheme.colors.onSurface
                            },
                            shape = NotDjinniTheme.shapes.small,
                        )
                        .background(
                            color = if (category == selectedCategory) {
                                NotDjinniTheme.colors.primary.copy(alpha = 0.5f)
                            } else {
                                NotDjinniTheme.colors.primary.copy(alpha = 0.3f)
                            },
                            shape = NotDjinniTheme.shapes.small,
                        )
                        .clickableNoRipple {
                            onAction(EditSeekerProfileAction.SelectJobCategory(category))
                        }
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
        }
    }
}

private fun DateRangePickerState.toFormatterMonthYearDate(): String {
    val startDate = selectedStartDateMillis.toFormatterMonthYearDate()
    val endDate = selectedEndDateMillis.toFormatterMonthYearDate()
    return "$startDate - $endDate"
}

private fun Long?.toFormatterMonthYearDate(): String {
    return this?.toInstant()?.toFormatterMonthYearDate() ?: "..."
}

@Composable
@Preview
private fun Preview() {
    NotDjinniTheme {
        Content(
            state = EditSeekerProfileState(isLoading = false),
            specialityFieldState = rememberTextFieldState(),
            desiredSalaryFieldState = rememberTextFieldState(),
            experienceFieldState = rememberTextFieldState(),
            aboutMeFieldState = rememberTextFieldState(),
            onAction = {},
        )
    }
}
