package not.djinni.presentation.screens.auth.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import not.djinni.R
import not.djinni.presentation.core.components.base.buildDefaultTextFieldDecorator
import not.djinni.presentation.core.components.base.model.TextData
import not.djinni.presentation.core.extension.clickableNoRipple
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.theme.NotDjinniIcons
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    textStyle: TextStyle = NotDjinniTheme.typography.body1.copy(
        color = NotDjinniTheme.colors.onSurface,
    ),
    cursorBrush: SolidColor = SolidColor(NotDjinniTheme.colors.onSurface),
    placeholder: TextData = R.string.password_field_placeholder.toTextData()
) {
    var isHidden by remember { mutableStateOf(true) }

    BasicSecureTextField(
        modifier = modifier,
        state = state,
        textStyle = textStyle,
        decorator = buildDefaultTextFieldDecorator(
            state = state,
            placeholder = placeholder,
            textStyle = textStyle,
            endIcon = {
                VisibilityIcon(
                    isHidden = isHidden,
                    onClick = { isHidden = !isHidden }
                )
            }
        ),
        cursorBrush = cursorBrush,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ),
        textObfuscationMode = if (isHidden) {
            TextObfuscationMode.Hidden
        } else {
            TextObfuscationMode.Visible
        },
    )
}

@Composable
private fun VisibilityIcon(
    modifier: Modifier = Modifier,
    isHidden: Boolean,
    onClick: () -> Unit
) {
    Icon(
        imageVector = if (isHidden) NotDjinniIcons.filledEye else NotDjinniIcons.eye,
        contentDescription = null,
        tint = NotDjinniTheme.colors.onSurface,
        modifier = modifier
            .size(24.dp)
            .clickableNoRipple(onClick = onClick)
    )
}

@Preview
@Composable
private fun Preview() {
    NotDjinniTheme {
        PasswordTextField(
            modifier = Modifier.fillMaxWidth(),
            state = TextFieldState()
        )
    }
}