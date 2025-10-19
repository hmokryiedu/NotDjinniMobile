package not.djinni.presentation.screens.example

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import not.djinni.presentation.core.Screen
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
fun ExampleWithStateScreen() {
    Screen<ExampleStateViewModel> { viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()

        Content(state = state)
    }
}

@Composable
private fun Content(state: ExampleState) {

}

@Composable
@Preview
private fun Preview() {
    NotDjinniTheme {
        val state = ExampleState()
        Content(state)
    }
}