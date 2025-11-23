package not.djinni.presentation.screens.employer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import not.djinni.presentation.core.Screen
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
internal fun MainEmployerScreen() {
    Screen<MainEmployerViewModel> { viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()

        Content(state = state)
    }
}

@Composable
private fun Content(state: MainEmployerState) {
    // Empty content
}

@Composable
@Preview
private fun Preview() {
    NotDjinniTheme {
        val state = MainEmployerState()
        Content(state = state)
    }
}
