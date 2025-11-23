package not.djinni.presentation.screens.employer.createvacancy

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import not.djinni.presentation.core.Screen
import not.djinni.presentation.core.extension.collectAsEffect
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
internal fun CreateVacancyScreen(
    onNavigateBack: () -> Unit,
) {
    Screen<CreateVacancyViewModel> { viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()

        Content(state = state)

        viewModel.sideEffect.collectAsEffect { effect ->
            when (effect) {
                CreateVacancySideEffect.NavigateBack -> onNavigateBack()
            }
        }
    }
}

@Composable
private fun Content(state: CreateVacancyState) {
    // TODO: Implement vacancy creation UI
}

@Composable
@Preview
private fun Preview() {
    NotDjinniTheme {
        val state = CreateVacancyState()
        Content(state = state)
    }
}
