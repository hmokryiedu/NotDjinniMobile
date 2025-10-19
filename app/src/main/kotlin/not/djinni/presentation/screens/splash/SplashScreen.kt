package not.djinni.presentation.screens.splash

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import not.djinni.presentation.core.Screen
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
fun SplashScreen() {
    Screen<SplashViewModel> { viewModel ->
        Content()
    }
}

@Composable
private fun Content() {

}

@Composable
@Preview
private fun Preview() {
    NotDjinniTheme { Content() }
}