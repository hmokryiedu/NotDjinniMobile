package not.djinni.presentation.core.extension

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.shimmerLoading(
    cornerRadius: Dp = 16.dp,
    isVisible: Boolean = true,
    durationMillis: Int = DEFAULT_SHIMMER_DURATION_MILLIS,
): Modifier = composed {
    if (!isVisible) return@composed this
    val translateAnimation by rememberShimmerAnimation(durationMillis)
    return@composed drawBehind {
        drawRoundRect(
            brush = createShimmerBrush(translateAnimation * size.width),
            cornerRadius = CornerRadius(cornerRadius.toPx())
        )
    }
}

private fun createShimmerBrush(translateAnimation: Float): Brush {
    return Brush.linearGradient(
        colors = listOf(
            Color.LightGray.copy(alpha = 0.2f),
            Color.LightGray.copy(alpha = 1.0f),
            Color.LightGray.copy(alpha = 0.2f),
        ),
        start = Offset(x = translateAnimation, y = translateAnimation),
        end = Offset(
            x = translateAnimation + SHIMMER_SIZE,
            y = translateAnimation + SHIMMER_SIZE
        ),
    )
}


@Composable
private fun rememberShimmerAnimation(durationMillis: Int = 3000): State<Float> {
    val transition = rememberInfiniteTransition(label = "")
    return transition.animateFloat(
        initialValue = INITIAL_SHIMMER_VALUE,
        targetValue = TARGET_SHIMMER_VALUE,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "",
    )
}

private const val SHIMMER_SIZE = 100

private const val DEFAULT_SHIMMER_DURATION_MILLIS = 3000

private const val INITIAL_SHIMMER_VALUE = 0f
private const val TARGET_SHIMMER_VALUE = 1f