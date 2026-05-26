package not.djinni.presentation.screens.seeker.vacancy.applied

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
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
import not.djinni.model.application.ApplicationStatus
import not.djinni.presentation.core.Screen
import not.djinni.presentation.core.components.base.FullscreenColumn
import not.djinni.presentation.core.components.base.HorizontalSpacer
import not.djinni.presentation.core.components.base.NotDjinniButton
import not.djinni.presentation.core.components.base.NotDjinniLoader
import not.djinni.presentation.core.components.base.NotDjinniText
import not.djinni.presentation.core.components.base.VacancyCard
import not.djinni.presentation.core.components.base.VerticalSpacer
import not.djinni.presentation.core.components.base.model.ButtonData
import not.djinni.presentation.core.extension.clickableNoRipple
import not.djinni.presentation.core.extension.collectAsEffect
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.screens.seeker.application.list.mapper.toDisplayStringRes
import not.djinni.presentation.screens.seeker.application.list.mapper.toThemeColor
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
        TopBar(
            onBack = { onAction(AppliedVacanciesAction.NavigateBack) },
            onOpenFilter = { onAction(AppliedVacanciesAction.OpenStatusFilter) },
            hasSelectedStatuses = state.selectedStatuses.isNotEmpty()
        )
        FilterSection(
            selectedStatuses = state.selectedStatuses,
            onRemoveStatus = { onAction(AppliedVacanciesAction.RemoveSelectedStatus(it)) }
        )
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
                    onAction(
                        AppliedVacanciesAction.OpenVacancy(
                            vacancyId
                        )
                    )
                }
            )
        }
    }
    StatusFilterDialog(
        state = state,
        onDismiss = { onAction(AppliedVacanciesAction.DismissStatusFilter) },
        onToggleStatus = { onAction(AppliedVacanciesAction.ToggleDraftStatus(it)) }
    )
}

@Composable
private fun TopBar(
    onBack: () -> Unit,
    onOpenFilter: () -> Unit,
    hasSelectedStatuses: Boolean,
) {
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
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            modifier = Modifier
                .size(ICON_SIZE)
                .clickableNoRipple(onClick = onOpenFilter),
            imageVector = Icons.Outlined.FilterList,
            contentDescription = null,
            tint = if (hasSelectedStatuses) {
                NotDjinniTheme.colors.primary
            } else {
                NotDjinniTheme.colors.onBackground
            }
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
    vacancies: List<AppliedVacancyCardData>,
    onVacancyClick: (Long) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .imePadding(),
        contentPadding = PaddingValues(horizontal = NotDjinniTheme.offsets.tiny),
        verticalArrangement = Arrangement.spacedBy(NotDjinniTheme.offsets.small)
    ) {
        items(
            items = vacancies,
            key = { it.vacancy.id }
        ) { vacancy ->
            VacancyCard(
                modifier = Modifier.animateItem(),
                data = vacancy.vacancy,
                onClick = { onVacancyClick(vacancy.vacancy.id) }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FilterSection(
    selectedStatuses: Set<ApplicationStatus>,
    onRemoveStatus: (ApplicationStatus) -> Unit,
) {
    Column {
        if (selectedStatuses.isNotEmpty()) {
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = NotDjinniTheme.offsets.small),
                horizontalArrangement = Arrangement.spacedBy(NotDjinniTheme.offsets.tiny),
                verticalArrangement = Arrangement.spacedBy(NotDjinniTheme.offsets.tiny)
            ) {
                selectedStatuses.forEach { status ->
                    AssistChip(
                        onClick = { onRemoveStatus(status) },
                        label = {
                            NotDjinniText(
                                data = status.toDisplayStringRes().toTextData(),
                                style = NotDjinniTheme.typography.body3
                            )
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = NotDjinniIcons.close,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = status.toThemeColor(NotDjinniTheme.colors)
                                .copy(alpha = 0.16f),
                            labelColor = status.toThemeColor(NotDjinniTheme.colors),
                            trailingIconContentColor = status.toThemeColor(NotDjinniTheme.colors)
                        ),
                        shape = NotDjinniTheme.shapes.great
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusFilterDialog(
    state: AppliedVacanciesState,
    onDismiss: () -> Unit,
    onToggleStatus: (ApplicationStatus) -> Unit,
) {
    if (!state.isStatusFilterVisible) return
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = NotDjinniTheme.shapes.large,
        containerColor = NotDjinniTheme.colors.surface,
        titleContentColor = NotDjinniTheme.colors.onSurface,
        textContentColor = NotDjinniTheme.colors.onSurface,
        title = {
            NotDjinniText(
                data = R.string.applied_vacancies_filter_dialog_title.toTextData(),
                style = NotDjinniTheme.typography.title2
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(NotDjinniTheme.offsets.tiny)) {
                ApplicationStatus.entries.forEach { status ->
                    StatusFilterRow(
                        status = status,
                        selected = status in state.draftStatuses,
                        onToggleStatus = onToggleStatus
                    )
                }
            }
        },
        confirmButton = {
            NotDjinniButton(
                modifier = Modifier.fillMaxWidth(),
                data = ButtonData(text = R.string.apply.toTextData()),
                onClick = onDismiss,
            )
        },
        dismissButton = null
    )
}

@Composable
private fun StatusFilterRow(
    status: ApplicationStatus,
    selected: Boolean,
    onToggleStatus: (ApplicationStatus) -> Unit,
) {
    val iconTint by animateColorAsState(
        targetValue = if (selected) {
            NotDjinniTheme.colors.primary
        } else {
            NotDjinniTheme.colors.onSurface
        },
        label = "statusRowIconTint"
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(NotDjinniTheme.colors.surface, shape = NotDjinniTheme.shapes.great)
            .clickableNoRipple { onToggleStatus(status) }
            .padding(
                horizontal = NotDjinniTheme.offsets.tiny,
                vertical = NotDjinniTheme.offsets.small
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NotDjinniText(
            modifier = Modifier.weight(1f),
            data = status.toDisplayStringRes().toTextData(),
            style = NotDjinniTheme.typography.body2,
            color = NotDjinniTheme.colors.onSurface
        )
        AnimatedContent(targetState = selected, label = "statusFilterIcon") { isSelected ->
            Icon(
                imageVector = if (isSelected) NotDjinniIcons.check else NotDjinniIcons.plus,
                contentDescription = null,
                tint = iconTint
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
