package not.djinni.presentation.screens.seeker.vacancy.applied

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import not.djinni.presentation.core.components.base.VacancyCard
import not.djinni.presentation.core.components.base.VerticalSpacer
import not.djinni.presentation.core.components.base.model.ButtonData
import not.djinni.presentation.core.components.base.model.VacancyCardData
import not.djinni.presentation.core.extension.clickableNoRipple
import not.djinni.presentation.core.extension.collectAsEffect
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.theme.NotDjinniIcons
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
internal fun AppliedVacanciesScreen(
    onBack: () -> Unit,
    onVacancyClick: (Long) -> Unit,
) {
    Screen<AppliedVacanciesViewModel> { viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()

        Content(
            state = state,
            onAction = viewModel::sendAction
        )

        viewModel.sideEffect.collectAsEffect { effect ->
            when (effect) {
                AppliedVacanciesSideEffect.NavigateBack -> onBack()
                is AppliedVacanciesSideEffect.NavigateToVacancyDetails -> onVacancyClick(effect.vacancyId)
            }
        }
    }
}

@Composable
private fun Content(
    state: AppliedVacanciesState,
    onAction: (AppliedVacanciesAction) -> Unit,
) {
    FullscreenColumn(modifier = Modifier.fillMaxSize()) {
        TopBar(onBack = { onAction(AppliedVacanciesAction.NavigateBack) })
        VerticalSpacer(NotDjinniTheme.offsets.medium)
        when (val contentState = state.contentState) {
            AppliedVacanciesContentState.Loading -> LoadingContent()
            AppliedVacanciesContentState.Empty -> EmptyContent()
            is AppliedVacanciesContentState.Error -> ErrorContent(
                message = contentState.message,
                onRetry = { onAction(AppliedVacanciesAction.Load) }
            )
            is AppliedVacanciesContentState.Data -> DataContent(
                vacancies = contentState.vacancies,
                onVacancyClick = { vacancyId ->
                    onAction(AppliedVacanciesAction.OpenVacancy(vacancyId))
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
            data = R.string.applied_vacancies_title.toTextData(),
            style = NotDjinniTheme.typography.title2.copy(fontWeight = FontWeight.Bold),
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
private fun EmptyContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        NotDjinniText(
            data = R.string.applied_vacancies_empty.toTextData(),
            style = NotDjinniTheme.typography.body2,
            color = NotDjinniTheme.colors.onBackground.copy(alpha = SECONDARY_TEXT_ALPHA)
        )
    }
}

@Composable
private fun ErrorContent(
    message: not.djinni.presentation.core.components.base.model.TextData,
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
    vacancies: List<VacancyCardData>,
    onVacancyClick: (Long) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding(),
        verticalArrangement = Arrangement.spacedBy(NotDjinniTheme.offsets.small)
    ) {
        items(
            items = vacancies,
            key = { it.id }
        ) { vacancy ->
            VacancyCard(
                modifier = Modifier.animateItem(),
                data = vacancy,
                onClick = { onVacancyClick(vacancy.id) }
            )
        }
    }
}

@Composable
@Preview
private fun Preview() {
    NotDjinniTheme {
        val state = AppliedVacanciesState(
            contentState = AppliedVacanciesContentState.Empty
        )
        Content(state = state, onAction = {})
    }
}

private val ICON_SIZE = 24.dp
private const val SECONDARY_TEXT_ALPHA = 0.6f
