package not.djinni.presentation.screens.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
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
import not.djinni.presentation.core.components.base.model.TextData
import not.djinni.presentation.core.extension.clickableNoRipple
import not.djinni.presentation.core.extension.collectAsEffect
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.screens.auth.components.PasswordTextField
import not.djinni.presentation.screens.auth.model.FieldsState
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
fun AuthScreen(
    onNext: () -> Unit,
    onPublic: () -> Unit,
) {
    Screen<AuthViewModel> { viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()
        val nameFieldState = rememberTextFieldState()
        val emailFieldState = rememberTextFieldState()
        val passwordFieldState = rememberTextFieldState()

        Content(
            state = state,
            nameFieldState = nameFieldState,
            emailFieldState = emailFieldState,
            passwordFieldState = passwordFieldState,
            onAction = viewModel::sendAction
        )

        viewModel.sideEffect.collectAsEffect { effect ->
            when (effect) {
                AuthSideEffect.NavigateNext -> onNext()
                AuthSideEffect.NavigatePublic -> onPublic()
            }
        }
    }
}

@Composable
private fun Content(
    state: AuthState,
    nameFieldState: TextFieldState,
    emailFieldState: TextFieldState,
    passwordFieldState: TextFieldState,
    onAction: (AuthAction) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    FullscreenColumn(
        modifier = Modifier.clickableNoRipple { keyboardController?.hide() },
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
        AnimatedVisibility(
            visible = state.type == AuthState.AuthType.SIGN_UP,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            NotDjinniTextField(
                modifier = Modifier
                    .padding(bottom = NotDjinniTheme.offsets.medium)
                    .fillMaxWidth(),
                state = nameFieldState,
                placeholder = R.string.full_name_placeholder.toTextData(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
        }
        NotDjinniTextField(
            modifier = Modifier.fillMaxWidth(),
            state = emailFieldState,
            lineLimits = TextFieldLineLimits.SingleLine,
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
            ),
            onClick = {
                val state = FieldsState(
                    name = nameFieldState.text.toString(),
                    email = emailFieldState.text.toString(),
                    password = passwordFieldState.text.toString(),
                )
                val action = AuthAction.AuthButtonClicked(state)
                onAction(action)
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
                .clickableNoRipple { onAction(AuthAction.SwitchAuthType) },
            data = state.type.switchText,
            style = NotDjinniTheme.typography.body3,
            color = NotDjinniTheme.colors.onSurface,
        )
        VerticalSpacer(NotDjinniTheme.offsets.medium)
        NotDjinniText(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickableNoRipple { onAction(AuthAction.ViewOnlyClicked) },
            data = R.string.auth_view_only.toTextData(),
            style = NotDjinniTheme.typography.body3,
            color = NotDjinniTheme.colors.onSurface,
        )
        AnimatedVisibility(
            modifier = Modifier
                .padding(top = NotDjinniTheme.offsets.huge)
                .align(Alignment.CenterHorizontally),
            visible = state.errorMessage != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            NotDjinniText(
                data = state.errorMessage ?: TextData.Empty,
                textAlign = TextAlign.Center,
                style = NotDjinniTheme.typography.body1,
                color = NotDjinniTheme.colors.error,
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
            emailFieldState = rememberTextFieldState(),
            nameFieldState = rememberTextFieldState(),
            passwordFieldState = rememberTextFieldState(),
            onAction = {}
        )
    }
}
