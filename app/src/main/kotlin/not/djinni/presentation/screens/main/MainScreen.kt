package not.djinni.presentation.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import not.djinni.R
import not.djinni.presentation.core.Screen
import not.djinni.presentation.core.components.base.NotDjinniText
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
fun MainScreen() {
    Screen<MainViewModel> { viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()

        Content(state = state)
    }
}

@Composable
private fun Content(state: MainState) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        NotDjinniText(
            data = R.string.app_name.toTextData(),
            style = NotDjinniTheme.typography.title1,
            color = NotDjinniTheme.colors.onSurface,
        )
    }
}

@Composable
@Preview
private fun Preview() {
    NotDjinniTheme {
        val state = MainState()
        Content(state)
    }
}
