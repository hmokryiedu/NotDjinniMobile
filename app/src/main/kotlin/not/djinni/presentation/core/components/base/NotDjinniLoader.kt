package not.djinni.presentation.core.components.base

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import not.djinni.presentation.core.DarkPreview
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
fun NotDjinniLoader(
    modifier: Modifier = Modifier,
    color: Color = NotDjinniTheme.colors.onBackground,
    trackColor: Color = NotDjinniTheme.colors.surfaceContainer.copy(alpha = 0.5f),
    strokeWidth: Dp = 4.dp
) {
    CircularProgressIndicator(
        modifier = modifier,
        color = color,
        trackColor = trackColor,
        strokeWidth = strokeWidth
    )
}

@DarkPreview
@Preview
@Composable
private fun NotDjinniLoaderPreview() {
    NotDjinniTheme {
        NotDjinniLoader()
    }
}