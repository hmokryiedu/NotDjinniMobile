@file:OptIn(ExperimentalMaterial3Api::class)

package not.djinni.presentation.screens.seeker.vacancy.details.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import not.djinni.R
import not.djinni.presentation.core.components.base.NotDjinniButton
import not.djinni.presentation.core.components.base.NotDjinniText
import not.djinni.presentation.core.components.base.NotDjinniTextField
import not.djinni.presentation.core.components.base.VerticalSpacer
import not.djinni.presentation.core.components.base.buildDefaultTextFieldDecorator
import not.djinni.presentation.core.components.base.model.ButtonData
import not.djinni.presentation.core.components.base.model.TextData
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
internal fun ApplyVacancyBottomSheet(
    vacancyName: TextData,
    onDismiss: () -> Unit,
    onApply: (coverLetter: String?) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coverLetterState = rememberTextFieldState()
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = {
            scope.launch {
                sheetState.hide()
                onDismiss()
            }
        },
        sheetState = sheetState,
        containerColor = NotDjinniTheme.colors.surface,
        contentColor = NotDjinniTheme.colors.onSurface,
    ) {
        BottomSheetContent(
            vacancyName = vacancyName,
            coverLetterState = coverLetterState,
            onApply = {
                scope.launch {
                    sheetState.hide()
                    onApply(coverLetterState.text.toString().ifBlank { null })
                }
            }
        )
    }
}

@Composable
private fun BottomSheetContent(
    vacancyName: TextData,
    coverLetterState: TextFieldState,
    onApply: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = NotDjinniTheme.offsets.medium)
            .padding(bottom = NotDjinniTheme.offsets.medium)
    ) {
        NotDjinniText(
            data = R.string.apply_vacancy_title.toTextData(),
            style = NotDjinniTheme.typography.title2,
            color = NotDjinniTheme.colors.onSurface
        )
        VerticalSpacer(NotDjinniTheme.offsets.tiny)
        NotDjinniText(
            data = vacancyName,
            style = NotDjinniTheme.typography.body1,
            color = NotDjinniTheme.colors.onSurface.copy(alpha = SECONDARY_TEXT_ALPHA)
        )
        VerticalSpacer(NotDjinniTheme.offsets.medium)
        NotDjinniText(
            data = R.string.apply_vacancy_cover_letter_title.toTextData(),
            style = NotDjinniTheme.typography.body3,
            color = NotDjinniTheme.colors.onSurface
        )
        VerticalSpacer(NotDjinniTheme.offsets.tiny)
        NotDjinniTextField(
            modifier = Modifier.fillMaxWidth(),
            state = coverLetterState,
            lineLimits = TextFieldLineLimits.MultiLine(
                minHeightInLines = COVER_LETTER_MIN_LINES,
                maxHeightInLines = COVER_LETTER_MAX_LINES
            ),
            decorator = buildDefaultTextFieldDecorator(
                state = coverLetterState,
                placeholder = R.string.apply_vacancy_cover_letter_hint.toTextData(),
                textStyle = NotDjinniTheme.typography.body1.copy(
                    color = NotDjinniTheme.colors.onSurface
                )
            )
        )
        VerticalSpacer(NotDjinniTheme.offsets.medium)
        NotDjinniButton(
            modifier = Modifier.fillMaxWidth(),
            data = ButtonData(text = R.string.apply_vacancy_button.toTextData()),
            onClick = onApply
        )
    }
}

@Composable
@Preview
private fun Preview() {
    NotDjinniTheme {
        BottomSheetContent(
            vacancyName = "Senior Android Developer".toTextData(),
            coverLetterState = rememberTextFieldState(),
            onApply = {}
        )
    }
}

private const val COVER_LETTER_MIN_LINES = 4
private const val COVER_LETTER_MAX_LINES = 8
private const val SECONDARY_TEXT_ALPHA = 0.7f
