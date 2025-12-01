package not.djinni.presentation.core.components.base

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldDecorator
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import not.djinni.R
import not.djinni.presentation.core.components.base.model.TextData
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
fun NotDjinniTextField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    decorator: TextFieldDecorator? = null,
    onKeyboardAction: KeyboardActionHandler? = null,
    inputTransformation: InputTransformation? = null,
    outputTransformation: OutputTransformation? = null,
    interactionSource: MutableInteractionSource? = null,
    textStyle: TextStyle = NotDjinniTheme.typography.body1.copy(
        color = NotDjinniTheme.colors.onSurface,
    ),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.Default,
    placeholder: TextData = R.string.default_placeholder.toTextData(),
    cursorBrush: SolidColor = SolidColor(NotDjinniTheme.colors.onSurface),
) {
    BasicTextField(
        modifier = modifier,
        state = state,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        decorator = decorator ?: buildDefaultTextFieldDecorator(
            state = state,
            placeholder = placeholder,
            textStyle = textStyle
        ),
        lineLimits = lineLimits,
        cursorBrush = cursorBrush,
        keyboardOptions = keyboardOptions,
        onKeyboardAction = onKeyboardAction,
        interactionSource = interactionSource,
        inputTransformation = inputTransformation,
        outputTransformation = outputTransformation,
    )
}

fun buildDefaultTextFieldDecorator(
    state: TextFieldState,
    placeholder: TextData,
    textStyle: TextStyle,
    endIcon: @Composable (() -> Unit)? = null,
) = TextFieldDecorator { innerTextField ->
    Row(
        modifier = Modifier
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
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f)) {
            innerTextField()
            if (state.text.isEmpty()) {
                NotDjinniText(
                    data = placeholder,
                    style = textStyle,
                    color = NotDjinniTheme.colors.onSurface.copy(alpha = 0.5f),
                )
            }
        }
        endIcon?.let {
            HorizontalSpacer(NotDjinniTheme.offsets.small)
            it()
        }
    }
}

@Preview
@Composable
private fun Preview() {
    NotDjinniTheme {
        NotDjinniTextField(
            modifier = Modifier.fillMaxWidth(),
            state = TextFieldState(),
        )
    }
}