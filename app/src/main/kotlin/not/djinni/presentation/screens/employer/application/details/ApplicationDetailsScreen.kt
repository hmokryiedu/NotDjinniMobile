package not.djinni.presentation.screens.employer.application.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import not.djinni.R
import not.djinni.presentation.core.Screen
import not.djinni.presentation.core.components.base.FullscreenColumn
import not.djinni.presentation.core.components.base.HorizontalSpacer
import not.djinni.presentation.core.components.base.NotDjinniButton
import not.djinni.presentation.core.components.base.NotDjinniLoader
import not.djinni.presentation.core.components.base.NotDjinniText
import not.djinni.presentation.core.components.base.VerticalSpacer
import not.djinni.presentation.core.components.base.model.ButtonData
import not.djinni.presentation.core.components.base.model.TextData
import not.djinni.presentation.core.extension.clickableNoRipple
import not.djinni.presentation.core.extension.collectAsEffect
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.screens.employer.application.details.components.UpdateStatusBottomSheet
import not.djinni.presentation.theme.NotDjinniIcons
import not.djinni.presentation.theme.NotDjinniTheme
import org.koin.core.parameter.parametersOf
import androidx.compose.ui.graphics.Color as ComposeColor

@Composable
internal fun ApplicationDetailsScreen(
    applicationId: Long,
    onNavigateBack: () -> Unit,
    onNavigateToVacancyDetails: (Long) -> Unit,
) {
    Screen<ApplicationDetailsViewModel>(
        parameters = { parametersOf(applicationId) }
    ) { viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()

        Content(
            state = state,
            onAction = viewModel::sendAction
        )

        viewModel.sideEffect.collectAsEffect { effect ->
            when (effect) {
                ApplicationDetailsSideEffect.NavigateBack -> onNavigateBack()
                is ApplicationDetailsSideEffect.NavigateToVacancyDetails -> {
                    onNavigateToVacancyDetails(effect.vacancyId)
                }
            }
        }
    }
}

@Composable
private fun Content(
    state: ApplicationDetailsState,
    onAction: (ApplicationDetailsAction) -> Unit = {},
) {
    FullscreenColumn {
        TopBar(onBack = { onAction(ApplicationDetailsAction.NavigateBack) })
        when (val contentState = state.contentState) {
            is ContentState.Loading -> LoadingContent()
            is ContentState.Error -> ErrorContent(
                message = contentState.message,
                onRetry = { onAction(ApplicationDetailsAction.Retry) }
            )
            is ContentState.Data -> DataContent(
                details = contentState.details,
                onViewVacancy = { onAction(ApplicationDetailsAction.ViewVacancyDetails) },
                onUpdateStatus = { onAction(ApplicationDetailsAction.OpenUpdateStatusSheet) }
            )
        }
    }

    if (state.openedAlert == ApplicationDetailsAlert.UPDATE_STATUS) {
        val currentState = state.contentState
        if (currentState is ContentState.Data) {
            UpdateStatusBottomSheet(
                currentStatus = currentState.details.currentStatus,
                onDismiss = { onAction(ApplicationDetailsAction.DismissAlert) },
                onSelect = { newStatus ->
                    onAction(ApplicationDetailsAction.UpdateStatus(newStatus))
                }
            )
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
            data = R.string.application_details_title.toTextData(),
            style = NotDjinniTheme.typography.title2,
            color = NotDjinniTheme.colors.onBackground
        )
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        NotDjinniLoader()
    }
}

@Composable
private fun ErrorContent(
    message: TextData,
    onRetry: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        NotDjinniText(
            data = message,
            style = NotDjinniTheme.typography.body1,
            color = NotDjinniTheme.colors.error
        )
        VerticalSpacer(NotDjinniTheme.offsets.medium)
        NotDjinniButton(
            data = ButtonData(text = R.string.retry.toTextData()),
            onClick = onRetry
        )
    }
}

@Composable
private fun DataContent(
    details: ApplicationDetailsDisplayData,
    onViewVacancy: () -> Unit,
    onUpdateStatus: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            StatusSection(
                status = details.status,
                statusColor = details.statusColor
            )
            VerticalSpacer(NotDjinniTheme.offsets.medium)
            JobSeekerSection(
                name = details.seekerName,
                speciality = details.seekerSpeciality,
                experience = details.seekerExperience
            )
            VerticalSpacer(NotDjinniTheme.offsets.medium)
            VacancySummarySection(
                title = details.vacancyTitle,
                salary = details.vacancySalary,
                employmentType = details.vacancyEmploymentType,
                onViewDetails = onViewVacancy
            )
            VerticalSpacer(NotDjinniTheme.offsets.medium)
            CoverLetterSection(coverLetter = details.coverLetter)
            VerticalSpacer(NotDjinniTheme.offsets.medium)
            TimelineSection(
                appliedDate = details.appliedDate,
                updatedDate = details.updatedDate
            )
            VerticalSpacer(NotDjinniTheme.offsets.large)
        }
        NotDjinniButton(
            modifier = Modifier.fillMaxWidth(),
            data = ButtonData(text = R.string.application_details_update_status.toTextData()),
            onClick = onUpdateStatus
        )
        VerticalSpacer(NotDjinniTheme.offsets.medium)
    }
}

@Composable
private fun StatusSection(
    status: TextData,
    statusColor: ComposeColor,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        NotDjinniText(
            data = R.string.application_details_status.toTextData(),
            style = NotDjinniTheme.typography.body2,
            color = NotDjinniTheme.colors.onBackground.copy(alpha = SECONDARY_TEXT_ALPHA)
        )
        VerticalSpacer(NotDjinniTheme.offsets.tiny)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(STATUS_INDICATOR_SIZE)
                    .background(
                        color = statusColor,
                        shape = CircleShape
                    )
            )
            HorizontalSpacer(NotDjinniTheme.offsets.small)
            NotDjinniText(
                data = status,
                style = NotDjinniTheme.typography.body1Bold,
                color = NotDjinniTheme.colors.onBackground
            )
        }
    }
}

@Composable
private fun JobSeekerSection(
    name: TextData,
    speciality: TextData,
    experience: TextData,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = NotDjinniTheme.colors.highlightedContainer,
                shape = NotDjinniTheme.shapes.medium
            )
            .padding(NotDjinniTheme.offsets.medium)
    ) {
        NotDjinniText(
            data = R.string.application_details_seeker_info.toTextData(),
            style = NotDjinniTheme.typography.title2,
            color = NotDjinniTheme.colors.onBackground
        )
        VerticalSpacer(NotDjinniTheme.offsets.small)
        NotDjinniText(
            data = name,
            style = NotDjinniTheme.typography.body1Bold,
            color = NotDjinniTheme.colors.onBackground
        )
        VerticalSpacer(NotDjinniTheme.offsets.tiny)
        NotDjinniText(
            data = speciality,
            style = NotDjinniTheme.typography.body2,
            color = NotDjinniTheme.colors.onBackground.copy(alpha = SECONDARY_TEXT_ALPHA)
        )
        VerticalSpacer(NotDjinniTheme.offsets.tiny)
        NotDjinniText(
            data = experience,
            style = NotDjinniTheme.typography.body2,
            color = NotDjinniTheme.colors.onBackground.copy(alpha = SECONDARY_TEXT_ALPHA)
        )
    }
}

@Composable
private fun VacancySummarySection(
    title: TextData,
    salary: TextData,
    employmentType: TextData,
    onViewDetails: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        NotDjinniText(
            data = R.string.application_details_vacancy_summary.toTextData(),
            style = NotDjinniTheme.typography.title2,
            color = NotDjinniTheme.colors.onBackground
        )
        VerticalSpacer(NotDjinniTheme.offsets.small)
        NotDjinniText(
            data = title,
            style = NotDjinniTheme.typography.body1Bold,
            color = NotDjinniTheme.colors.onBackground
        )
        VerticalSpacer(NotDjinniTheme.offsets.tiny)
        Row(verticalAlignment = Alignment.CenterVertically) {
            NotDjinniText(
                data = salary,
                style = NotDjinniTheme.typography.body2,
                color = NotDjinniTheme.colors.onBackground
            )
            HorizontalSpacer(NotDjinniTheme.offsets.small)
            NotDjinniText(
                data = employmentType,
                style = NotDjinniTheme.typography.body2,
                color = NotDjinniTheme.colors.onBackground.copy(alpha = SECONDARY_TEXT_ALPHA)
            )
        }
        VerticalSpacer(NotDjinniTheme.offsets.small)
        NotDjinniText(
            modifier = Modifier.clickableNoRipple(onClick = onViewDetails),
            data = R.string.application_details_view_vacancy.toTextData(),
            style = NotDjinniTheme.typography.body2,
            color = NotDjinniTheme.colors.primary
        )
    }
}

@Composable
private fun CoverLetterSection(coverLetter: TextData?) {
    Column(modifier = Modifier.fillMaxWidth()) {
        NotDjinniText(
            data = R.string.application_details_cover_letter.toTextData(),
            style = NotDjinniTheme.typography.title2,
            color = NotDjinniTheme.colors.onBackground
        )
        VerticalSpacer(NotDjinniTheme.offsets.small)
        NotDjinniText(
            data = coverLetter ?: R.string.application_details_no_cover_letter.toTextData(),
            style = NotDjinniTheme.typography.body2.copy(fontWeight = FontWeight.Thin),
            color = if (coverLetter != null) {
                NotDjinniTheme.colors.onBackground
            } else {
                NotDjinniTheme.colors.onBackground.copy(alpha = SECONDARY_TEXT_ALPHA)
            }
        )
    }
}

@Composable
private fun TimelineSection(
    appliedDate: TextData,
    updatedDate: TextData,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        NotDjinniText(
            data = R.string.application_details_timeline.toTextData(),
            style = NotDjinniTheme.typography.title2,
            color = NotDjinniTheme.colors.onBackground
        )
        VerticalSpacer(NotDjinniTheme.offsets.small)
        Row {
            NotDjinniText(
                data = R.string.application_details_applied_at.toTextData(),
                style = NotDjinniTheme.typography.body2,
                color = NotDjinniTheme.colors.onBackground.copy(alpha = SECONDARY_TEXT_ALPHA)
            )
            HorizontalSpacer(NotDjinniTheme.offsets.tiny)
            NotDjinniText(
                data = appliedDate,
                style = NotDjinniTheme.typography.body2,
                color = NotDjinniTheme.colors.onBackground
            )
        }
        VerticalSpacer(NotDjinniTheme.offsets.tiny)
        Row {
            NotDjinniText(
                data = R.string.application_details_updated_at.toTextData(),
                style = NotDjinniTheme.typography.body2,
                color = NotDjinniTheme.colors.onBackground.copy(alpha = SECONDARY_TEXT_ALPHA)
            )
            HorizontalSpacer(NotDjinniTheme.offsets.tiny)
            NotDjinniText(
                data = updatedDate,
                style = NotDjinniTheme.typography.body2,
                color = NotDjinniTheme.colors.onBackground
            )
        }
    }
}

@Composable
@Preview
private fun Preview() {
    NotDjinniTheme {
        val state = ApplicationDetailsState(
            contentState = ContentState.Data(
                ApplicationDetailsDisplayData(
                    applicationId = 1,
                    currentStatus = not.djinni.model.application.ApplicationStatus.APPLIED,
                    status = "Applied".toTextData(),
                    statusColor = ComposeColor.White,
                    seekerName = "John Doe".toTextData(),
                    seekerSpeciality = "Android Developer".toTextData(),
                    seekerExperience = "5 years of experience".toTextData(),
                    vacancyTitle = "Senior Android Developer".toTextData(),
                    vacancySalary = "$5000 - $8000".toTextData(),
                    vacancyEmploymentType = "Full-time".toTextData(),
                    coverLetter = "I am very interested in this position...".toTextData(),
                    appliedDate = "Dec 1, 2025".toTextData(),
                    updatedDate = "Dec 2, 2025".toTextData(),
                    vacancyId = 1
                )
            )
        )
        Content(state = state)
    }
}

private val ICON_SIZE = 24.dp
private val STATUS_INDICATOR_SIZE = 12.dp
private const val SECONDARY_TEXT_ALPHA = 0.7f
