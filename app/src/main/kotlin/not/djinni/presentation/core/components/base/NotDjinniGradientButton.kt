package not.djinni.presentation.core.components.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import not.djinni.presentation.core.components.base.model.TextData
import not.djinni.presentation.core.extension.clickableNoRipple
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.theme.NotDjinniTheme

@Composable
fun NotDjinniGradientButton(
    modifier: Modifier = Modifier,
    text: TextData,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(NotDjinniTheme.shapes.large)
            .background(NotDjinniTheme.colors.primary)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0f),
                        Color.White.copy(alpha = 0.16f)
                    )
                )
            )
            .padding(NotDjinniTheme.offsets.medium)
            .clickableNoRipple(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        NotDjinniText(
            data = text,
            style = NotDjinniTheme.typography.body1.copy(
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        )
    }
}

@Preview
@Composable
private fun Preview() {
    NotDjinniTheme {
        NotDjinniGradientButton(
            text = "NotDjinni Button".toTextData(),
            onClick = {}
        )
    }
}
