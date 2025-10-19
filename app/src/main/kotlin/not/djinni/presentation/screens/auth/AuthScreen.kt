package not.djinni.presentation.screens.auth

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import not.djinni.R
import not.djinni.presentation.core.Screen
import not.djinni.presentation.core.components.base.FullscreenColumn
import not.djinni.presentation.core.components.base.NotDjinniButton
import not.djinni.presentation.core.components.base.NotDjinniText
import not.djinni.presentation.core.components.base.NotDjinniTextField
import not.djinni.presentation.core.components.base.VerticalSpacer
import not.djinni.presentation.core.components.base.buildFullscreenColumnPadding
import not.djinni.presentation.core.components.base.model.ButtonData
import not.djinni.presentation.core.extension.clickableNoRipple
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.screens.auth.components.PasswordTextField
import not.djinni.presentation.screens.auth.extension.isEmailValid
import not.djinni.presentation.screens.auth.extension.isPasswordValid
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
fun AuthScreen() {
    Screen<AuthViewModel> { viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()

        Content(
            state = state,
            onLogin = viewModel::onLogin,
            onTypeSwitch = viewModel::onAuthTypeChange,
        )
    }
}

@Composable
private fun Content(
    state: AuthState,
    onTypeSwitch: () -> Unit,
    onLogin: (String, String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val emailFieldState = rememberTextFieldState()
    val passwordFieldState = rememberTextFieldState()
    val isSignInEnabled by remember(emailFieldState, passwordFieldState) {
        derivedStateOf { emailFieldState.text.isEmailValid() && passwordFieldState.text.isPasswordValid() }
    }

    FullscreenColumn(
        modifier = Modifier.clickableNoRipple {
            keyboardController?.hide()
        },
        contentPadding = buildFullscreenColumnPadding(
            vertical = NotDjinniTheme.offsets.medium,
            horizontal = NotDjinniTheme.offsets.large,
        ),
        horizontalAlignment = Alignment.Start,
    ) {
        NotDjinniText(
            data = R.string.general_auth_title.toTextData(),
            style = NotDjinniTheme.typography.title1,
            color = NotDjinniTheme.colors.onSurface,
        )
        VerticalSpacer(NotDjinniTheme.offsets.huge)
        NotDjinniText(
            data = state.type.titleText,
            style = NotDjinniTheme.typography.title1,
            color = NotDjinniTheme.colors.onSurface,
        )
        VerticalSpacer(NotDjinniTheme.offsets.medium)
        NotDjinniTextField(
            modifier = Modifier.fillMaxWidth(),
            state = emailFieldState,
            placeholder = R.string.email_field_placeholder.toTextData(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        VerticalSpacer(NotDjinniTheme.offsets.small)
        PasswordTextField(
            modifier = Modifier.fillMaxWidth(),
            state = passwordFieldState,
        )
        VerticalSpacer(NotDjinniTheme.offsets.medium)
        NotDjinniButton(
            modifier = Modifier.fillMaxWidth(),
            data = ButtonData(
                text = state.type.buttonText,
                enabled = isSignInEnabled
            ),
            onClick = {
                onLogin(emailFieldState.text.toString(), passwordFieldState.text.toString())
            },
        )
        VerticalSpacer(NotDjinniTheme.offsets.medium)
        HorizontalDivider(
            modifier = Modifier
                .padding(
                    horizontal = NotDjinniTheme.offsets.tiny,
                    vertical = NotDjinniTheme.offsets.medium
                )
                .fillMaxWidth(),
            color = NotDjinniTheme.colors.onSurface
        )
        NotDjinniText(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickableNoRipple(onClick = onTypeSwitch),
            data = state.type.switchText,
            style = NotDjinniTheme.typography.body3,
            color = NotDjinniTheme.colors.onSurface,
        )
        state.errorMessage?.let {
            VerticalSpacer(NotDjinniTheme.offsets.huge)
            NotDjinniText(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                data = state.errorMessage,
                style = NotDjinniTheme.typography.body1,
                color = Color(0xFFFF0000),
            )
        }
    }
}

@Composable
@Preview
private fun Preview() {
    NotDjinniTheme {
        val state = AuthState()
        Content(
            state = state,
            onLogin = { _, _ -> },
            onTypeSwitch = {},
        )
    }
}
