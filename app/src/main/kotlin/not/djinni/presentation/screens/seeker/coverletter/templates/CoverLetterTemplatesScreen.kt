package not.djinni.presentation.screens.seeker.coverletter.templates

import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.ui.window.DialogProperties
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.result.LocalResultEventBus
import not.djinni.R
import not.djinni.presentation.core.Screen
import not.djinni.presentation.core.components.base.FullscreenColumn
import not.djinni.presentation.core.components.base.HorizontalSpacer
import not.djinni.presentation.core.components.base.NotDjinniButton
import not.djinni.presentation.core.components.base.NotDjinniLoader
import not.djinni.presentation.core.components.base.NotDjinniText
import not.djinni.presentation.core.components.base.VerticalSpacer
import not.djinni.presentation.core.components.base.model.ButtonData
import not.djinni.presentation.core.extension.clickableNoRipple
import not.djinni.presentation.core.extension.collectAsEffect
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.theme.NotDjinniIcons
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
internal fun CoverLetterTemplatesScreen(
    resultKey: String,
    onBack: () -> Unit,
) {
    Screen<CoverLetterTemplatesViewModel> { viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()
        val resultEventBus = LocalResultEventBus.current

        Content(
            state = state,
            onAction = viewModel::sendAction
        )

        TemplateDialog(state = state, onAction = viewModel::sendAction)

        viewModel.sideEffect.collectAsEffect { effect ->
            when (effect) {
                CoverLetterTemplatesSideEffect.NavigateBack -> onBack()
                is CoverLetterTemplatesSideEffect.ApplyTemplate -> {
                    resultEventBus.sendResult(resultKey, effect.message)
                    onBack()
                }
            }
        }
    }
}

@Composable
private fun Content(
    state: CoverLetterTemplatesState,
    onAction: (CoverLetterTemplatesAction) -> Unit,
) {
    FullscreenColumn(modifier = Modifier.fillMaxSize()) {
        TopBar(
            onBack = { onAction(CoverLetterTemplatesAction.NavigateBack) },
            onAdd = { onAction(CoverLetterTemplatesAction.CreateTemplate) }
        )
        when (val contentState = state.contentState) {
            CoverLetterTemplatesContentState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    NotDjinniLoader()
                }
            }

            CoverLetterTemplatesContentState.Empty -> {
                EmptyState(onCreate = { onAction(CoverLetterTemplatesAction.CreateTemplate) })
            }

            is CoverLetterTemplatesContentState.Error -> {
                ErrorState(message = contentState.message, onRetry = { onAction(CoverLetterTemplatesAction.Load) })
            }

            is CoverLetterTemplatesContentState.Data -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(NotDjinniTheme.offsets.small)
                ) {
                    items(contentState.templates, key = { it.id }) { item ->
                        TemplateItem(item = item, onClick = { onAction(CoverLetterTemplatesAction.OpenTemplate(item.id)) })
                    }
                }
            }
        }
    }
}

@Composable
private fun TopBar(
    onBack: () -> Unit,
    onAdd: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = NotDjinniTheme.offsets.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(24.dp).clickableNoRipple(onClick = onBack),
            imageVector = NotDjinniIcons.back,
            contentDescription = null,
            tint = NotDjinniTheme.colors.onBackground
        )
        HorizontalSpacer(NotDjinniTheme.offsets.small)
        NotDjinniText(
            data = R.string.cover_letter_templates_title.toTextData(),
            style = NotDjinniTheme.typography.title2,
            color = NotDjinniTheme.colors.onBackground
        )
        Box(modifier = Modifier.weight(1f))
        Icon(
            modifier = Modifier.size(24.dp).clickableNoRipple(onClick = onAdd),
            imageVector = NotDjinniIcons.plus,
            contentDescription = null,
            tint = NotDjinniTheme.colors.onBackground
        )
    }
}

@Composable
private fun TemplateItem(
    item: CoverLetterTemplateDisplayData,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = NotDjinniTheme.colors.onSurface,
                shape = NotDjinniTheme.shapes.small
            )
            .background(
                color = NotDjinniTheme.colors.highlightedContainer,
                shape = NotDjinniTheme.shapes.small
            )
            .clickable(onClick = onClick)
            .padding(
                horizontal = NotDjinniTheme.offsets.medium,
                vertical = NotDjinniTheme.offsets.small
            )
    ) {
        NotDjinniText(
            data = item.message.toTextData(),
            style = NotDjinniTheme.typography.body2,
            color = NotDjinniTheme.colors.onBackground,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun EmptyState(onCreate: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        NotDjinniText(
            data = R.string.cover_letter_templates_empty.toTextData(),
            style = NotDjinniTheme.typography.body1,
            color = NotDjinniTheme.colors.onBackground
        )
        VerticalSpacer(NotDjinniTheme.offsets.medium)
        NotDjinniButton(
            data = ButtonData(text = R.string.cover_letter_templates_create.toTextData()),
            onClick = onCreate
        )
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        NotDjinniText(
            data = message.toTextData(),
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
private fun TemplateDialog(
    state: CoverLetterTemplatesState,
    onAction: (CoverLetterTemplatesAction) -> Unit,
) {
    if (state.selectedTemplate == null && !state.isEditing) return
    AlertDialog(
        onDismissRequest = { onAction(CoverLetterTemplatesAction.DismissDialog) },
        properties = DialogProperties(dismissOnClickOutside = true),
        containerColor = NotDjinniTheme.colors.surface,
        titleContentColor = NotDjinniTheme.colors.onSurface,
        textContentColor = NotDjinniTheme.colors.onSurface,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                NotDjinniText(
                    modifier = Modifier.weight(1f),
                    data = R.string.cover_letter_templates_dialog_title.toTextData(),
                    style = NotDjinniTheme.typography.title2,
                    color = NotDjinniTheme.colors.onSurface
                )
                if (!state.isEditing && state.selectedTemplate != null) {
                    Icon(
                        modifier = Modifier
                            .size(24.dp)
                            .clickableNoRipple(onClick = { onAction(CoverLetterTemplatesAction.Delete) }),
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = null,
                        tint = NotDjinniTheme.colors.error
                    )
                }
            }
        },
        text = {
            if (state.isEditing) {
                BasicTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.editingMessage,
                    onValueChange = { onAction(CoverLetterTemplatesAction.UpdateEditingMessage(it)) },
                    textStyle = NotDjinniTheme.typography.body1.copy(color = NotDjinniTheme.colors.onSurface)
                )
            } else {
                NotDjinniText(
                    data = state.selectedTemplate?.message.orEmpty().toTextData(),
                    style = NotDjinniTheme.typography.body1,
                    color = NotDjinniTheme.colors.onSurface
                )
            }
        },
        confirmButton = {
            when {
                state.isEditing -> {
                    NotDjinniButton(
                        modifier = Modifier.fillMaxWidth(),
                        data = ButtonData(
                            text = R.string.save.toTextData(),
                            enabled = state.editingMessage.trim().length >= CoverLetterTemplatesViewModel.MIN_TEMPLATE_LENGTH
                        ),
                        onClick = { onAction(CoverLetterTemplatesAction.Save) }
                    )
                }
                state.selectedTemplate != null -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(NotDjinniTheme.offsets.small)
                    ) {
                        NotDjinniButton(
                            modifier = Modifier.weight(1f),
                            data = ButtonData(text = R.string.edit.toTextData()),
                            onClick = { onAction(CoverLetterTemplatesAction.StartEdit) }
                        )
                        NotDjinniButton(
                            modifier = Modifier.weight(1f),
                            data = ButtonData(text = R.string.apply.toTextData()),
                            onClick = { onAction(CoverLetterTemplatesAction.Apply) }
                        )
                    }
                }
            }
        },
        dismissButton = null
    )
}
