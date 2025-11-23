package not.djinni.presentation.screens.employer.vacancydetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import not.djinni.presentation.core.Screen
import not.djinni.presentation.core.extension.collectAsEffect
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
internal fun VacancyDetailsScreen() {
    Screen<VacancyDetailsViewModel> { viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()

        Content(state = state)

        viewModel.sideEffect.collectAsEffect { effect ->

        }
    }
}

@Composable
private fun Content(state: VacancyDetailsState) {
}

@Composable
@Preview
private fun Preview() {
    NotDjinniTheme {
        val state = VacancyDetailsState()
        Content(state = state)
    }
}
