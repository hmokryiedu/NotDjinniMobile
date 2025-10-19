package not.djinni.presentation.screens.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import not.djinni.presentation.core.Screen
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
fun AuthScreen() {
    Screen<AuthViewModel> { viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()

        Content(state = state)
    }
}

@Composable
private fun Content(state: AuthState) {
    // UI implementation
}

@Composable
@Preview
private fun Preview() {
    NotDjinniTheme {
        val state = AuthState()
        Content(state)
    }
}
