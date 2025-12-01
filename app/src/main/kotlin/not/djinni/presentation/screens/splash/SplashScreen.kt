package not.djinni.presentation.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import not.djinni.R
import not.djinni.presentation.core.Screen
import not.djinni.presentation.core.extension.collectAsEffect
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
fun SplashScreen(
    onAuth: () -> Unit,
    onChooseRole: () -> Unit
) {
    Screen<SplashViewModel> { viewModel ->
        Content()

        viewModel.sideEffect.collectAsEffect { effect ->
            when (effect) {
                SplashSideEffect.NavigateToAuth -> onAuth()
                SplashSideEffect.NavigateToChooseRole -> onChooseRole()
            }
        }
    }
}

@Composable
private fun Content() {
    Box(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .size(190.dp),
            painter = painterResource(R.drawable.not_djinni_splash),
            contentDescription = null
        )
    }
}

@Composable
@Preview
private fun Preview() {
    NotDjinniTheme { Content() }
}