package not.djinni.presentation.screens.employer.vacancy.applications

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import not.djinni.R
import not.djinni.presentation.core.Screen
import not.djinni.presentation.core.components.base.ApplicationCard
import not.djinni.presentation.core.components.base.FullscreenColumn
import not.djinni.presentation.core.components.base.HorizontalSpacer
import not.djinni.presentation.core.components.base.NotDjinniButton
import not.djinni.presentation.core.components.base.NotDjinniLoader
import not.djinni.presentation.core.components.base.NotDjinniText
import not.djinni.presentation.core.components.base.VerticalSpacer
import not.djinni.presentation.core.components.base.model.ApplicationCardData
import not.djinni.presentation.core.components.base.model.ButtonData
import not.djinni.presentation.core.components.base.model.TextData
import not.djinni.presentation.core.extension.clickableNoRipple
import not.djinni.presentation.core.extension.collectAsEffect
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.theme.NotDjinniIcons
import not.djinni.presentation.theme.NotDjinniTheme
import org.koin.core.parameter.parametersOf

@Composable
internal fun ViewVacancyApplicationsScreen(
    vacancyId: Long,
    onNavigateBack: () -> Unit,
    onNavigateToApplicationDetails: (Long) -> Unit,
) {
    Screen<ViewVacancyApplicationsViewModel>(
        parameters = { parametersOf(vacancyId) }
    ) { viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()

        Content(
            state = state,
            onAction = viewModel::sendAction
        )

        viewModel.sideEffect.collectAsEffect { effect ->
            when (effect) {
                ViewVacancyApplicationsSideEffect.NavigateBack -> onNavigateBack()
                is ViewVacancyApplicationsSideEffect.NavigateToApplicationDetails -> {
                    onNavigateToApplicationDetails(effect.applicationId)
                }
            }
        }

        LaunchedEffect(Unit) {
            viewModel.sendAction(ViewVacancyApplicationsAction.LoadApplications)
        }
    }
}

@Composable
private fun Content(
    state: ViewVacancyApplicationsState,
    onAction: (ViewVacancyApplicationsAction) -> Unit = {},
) {
    FullscreenColumn {
        TopBar(onBack = { onAction(ViewVacancyApplicationsAction.NavigateBack) })
        when (val contentState = state.contentState) {
            is ApplicationsContentState.Loading -> LoadingContent()
            is ApplicationsContentState.Error -> ErrorContent(
                message = contentState.message,
                onRetry = { onAction(ViewVacancyApplicationsAction.LoadApplications) }
            )
            is ApplicationsContentState.Empty -> EmptyContent()
            is ApplicationsContentState.Data -> DataContent(
                applications = contentState.applications,
                onApplicationClick = { applicationId ->
                    onAction(ViewVacancyApplicationsAction.OpenApplicationDetails(applicationId))
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
            data = R.string.vacancy_applications_title.toTextData(),
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
private fun EmptyContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        NotDjinniText(
            data = R.string.vacancy_applications_empty.toTextData(),
            style = NotDjinniTheme.typography.body2,
            color = NotDjinniTheme.colors.onBackground.copy(alpha = 0.6f)
        )
    }
}

@Composable
private fun DataContent(
    applications: List<ApplicationCardData>,
    onApplicationClick: (Long) -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(NotDjinniTheme.offsets.small)
    ) {
        items(
            items = applications,
            key = { it.id }
        ) { application ->
            ApplicationCard(
                modifier = Modifier.animateItem(),
                data = application,
                onClick = { onApplicationClick(application.id) }
            )
        }
    }
}

@Composable
@Preview
private fun Preview() {
    NotDjinniTheme {
        val state = ViewVacancyApplicationsState(
            contentState = ApplicationsContentState.Empty
        )
        Content(state = state)
    }
}

private val ICON_SIZE = 24.dp
