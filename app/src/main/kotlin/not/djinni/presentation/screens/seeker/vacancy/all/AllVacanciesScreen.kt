package not.djinni.presentation.screens.seeker.vacancy.all

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import not.djinni.presentation.core.Screen
import not.djinni.presentation.core.extension.collectAsEffect
import not.djinni.presentation.screens.seeker.allvacancies.AllVacanciesState
import not.djinni.presentation.screens.seeker.allvacancies.AllVacanciesViewModel
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
internal fun AllVacanciesScreen() {
    Screen<AllVacanciesViewModel> { viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()

        Content(state = state)

        viewModel.sideEffect.collectAsEffect { effect ->

        }
    }
}

@Composable
private fun Content(state: AllVacanciesState) {
}

@Composable
@Preview
private fun Preview() {
    NotDjinniTheme {
        val state = AllVacanciesState()
        Content(state = state)
    }
}
