package not.djinni.presentation.screens.employer.profile.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import not.djinni.presentation.core.Screen
import not.djinni.presentation.core.extension.collectAsEffect
import not.djinni.presentation.screens.employer.viewjobseekerprofile.ViewJobSeekerProfileState
import not.djinni.presentation.screens.employer.viewjobseekerprofile.ViewJobSeekerProfileViewModel
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
internal fun ViewJobSeekerProfileScreen() {
    Screen<ViewJobSeekerProfileViewModel> { viewModel ->
        val state by viewModel.state.collectAsStateWithLifecycle()

        Content(state = state)

        viewModel.sideEffect.collectAsEffect { effect ->

        }
    }
}

@Composable
private fun Content(state: ViewJobSeekerProfileState) {
}

@Composable
@Preview
private fun Preview() {
    NotDjinniTheme {
        val state = ViewJobSeekerProfileState()
        Content(state = state)
    }
}
