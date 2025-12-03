package not.djinni.presentation.screens.employer.vacancy.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
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
import not.djinni.presentation.theme.NotDjinniIcons
import not.djinni.presentation.theme.NotDjinniTheme
import org.koin.core.parameter.parametersOf

@Composable
internal fun VacancyDetailsScreen(
    vacancyId: Long,
    onNavigateBack: () -> Unit,
    onNavigateToApplications: (Long) -> Unit,
) {
    Screen<VacancyDetailsViewModel>(
        parameters = { parametersOf(vacancyId) }
    ) { viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()

        Content(
            state = state,
            onAction = viewModel::sendAction
        )

        viewModel.sideEffect.collectAsEffect { effect ->
            when (effect) {
                VacancyDetailsSideEffect.NavigateBack -> onNavigateBack()
                is VacancyDetailsSideEffect.NavigateToApplications -> onNavigateToApplications(
                    effect.vacancyId
                )
            }
        }
    }
}

@Composable
private fun Content(
    state: VacancyDetailsState,
    onAction: (VacancyDetailsAction) -> Unit = {},
) {
    FullscreenColumn {
        TopBar(onBack = { onAction(VacancyDetailsAction.NavigateBack) })
        when (val contentState = state.contentState) {
            is VacancyDetailsContentState.Loading -> LoadingContent()
            is VacancyDetailsContentState.Error -> ErrorContent(
                message = contentState.message,
                onRetry = { onAction(VacancyDetailsAction.Retry) }
            )

            is VacancyDetailsContentState.Data -> DataContent(
                vacancy = contentState.vacancy,
                onViewApplications = { onAction(VacancyDetailsAction.ViewApplications) }
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
            data = R.string.vacancy_details_title.toTextData(),
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
    vacancy: VacancyDisplayData,
    onViewApplications: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            VacancyHeader(vacancy = vacancy)
            VerticalSpacer(NotDjinniTheme.offsets.medium)
            DescriptionSection(description = vacancy.description)
            VerticalSpacer(NotDjinniTheme.offsets.medium)
            DateInfoSection(
                postedDate = vacancy.postedDate,
                updatedDate = vacancy.updatedDate
            )
            VerticalSpacer(NotDjinniTheme.offsets.large)
        }
        NotDjinniButton(
            modifier = Modifier.fillMaxWidth(),
            data = ButtonData(text = R.string.vacancy_view_applications.toTextData()),
            onClick = onViewApplications
        )
        VerticalSpacer(NotDjinniTheme.offsets.medium)
    }
}

@Composable
private fun VacancyHeader(vacancy: VacancyDisplayData) {
    Column(modifier = Modifier.fillMaxWidth()) {
        NotDjinniText(
            data = vacancy.title,
            style = NotDjinniTheme.typography.title1Bold,
            color = NotDjinniTheme.colors.onBackground
        )
        VerticalSpacer(NotDjinniTheme.offsets.small)
        Row(verticalAlignment = Alignment.CenterVertically) {
            NotDjinniText(
                data = R.string.vacancy_status.toTextData(),
                style = NotDjinniTheme.typography.body2,
                color = NotDjinniTheme.colors.onBackground.copy(alpha = SECONDARY_TEXT_ALPHA)
            )
            HorizontalSpacer(NotDjinniTheme.offsets.tiny)
            NotDjinniText(
                data = vacancy.status,
                style = NotDjinniTheme.typography.body2,
                color = NotDjinniTheme.colors.primary
            )
        }
        VerticalSpacer(NotDjinniTheme.offsets.small)
        Row(verticalAlignment = Alignment.CenterVertically) {
            NotDjinniText(
                data = vacancy.salaryRange,
                style = NotDjinniTheme.typography.body1Bold,
                color = NotDjinniTheme.colors.onBackground
            )
            HorizontalSpacer(NotDjinniTheme.offsets.small)
            NotDjinniText(
                data = vacancy.employmentType,
                style = NotDjinniTheme.typography.body2,
                color = NotDjinniTheme.colors.onBackground.copy(alpha = SECONDARY_TEXT_ALPHA)
            )
        }
        VerticalSpacer(NotDjinniTheme.offsets.tiny)
        NotDjinniText(
            data = vacancy.requiredExperience,
            style = NotDjinniTheme.typography.body2,
            color = NotDjinniTheme.colors.onBackground.copy(alpha = SECONDARY_TEXT_ALPHA)
        )
    }
}

@Composable
private fun DescriptionSection(description: TextData) {
    Column(modifier = Modifier.fillMaxWidth()) {
        NotDjinniText(
            data = R.string.vacancy_details_description.toTextData(),
            style = NotDjinniTheme.typography.title2,
            color = NotDjinniTheme.colors.onBackground
        )
        VerticalSpacer(NotDjinniTheme.offsets.small)
        NotDjinniText(
            data = description,
            style = NotDjinniTheme.typography.body2.copy(fontWeight = FontWeight.Thin),
            color = NotDjinniTheme.colors.onBackground
        )
    }
}

@Composable
private fun DateInfoSection(
    postedDate: TextData,
    updatedDate: TextData,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row {
            NotDjinniText(
                data = R.string.vacancy_posted.toTextData(),
                style = NotDjinniTheme.typography.body2,
                color = NotDjinniTheme.colors.onBackground.copy(alpha = SECONDARY_TEXT_ALPHA)
            )
            NotDjinniText(
                data = postedDate,
                style = NotDjinniTheme.typography.body2,
                color = NotDjinniTheme.colors.onBackground
            )
        }
        VerticalSpacer(NotDjinniTheme.offsets.tiny)
        Row {
            NotDjinniText(
                data = R.string.vacancy_last_updated.toTextData(),
                style = NotDjinniTheme.typography.body2,
                color = NotDjinniTheme.colors.onBackground.copy(alpha = SECONDARY_TEXT_ALPHA)
            )
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
        val state = VacancyDetailsState(
            contentState = VacancyDetailsContentState.Data(
                vacancy = VacancyDisplayData(
                    id = 1,
                    title = "Senior Android Developer".toTextData(),
                    description = "We are looking for an experienced Android developer...".toTextData(),
                    salaryRange = "$5000 - $8000".toTextData(),
                    employmentType = "Full-time".toTextData(),
                    requiredExperience = "5+ years experience".toTextData(),
                    category = "Software Development".toTextData(),
                    status = "Active".toTextData(),
                    postedDate = "Dec 1, 2025".toTextData(),
                    updatedDate = "Dec 1, 2025".toTextData()
                )
            )
        )
        Content(state = state)
    }
}

private val ICON_SIZE = 24.dp
private const val SECONDARY_TEXT_ALPHA = 0.7f
