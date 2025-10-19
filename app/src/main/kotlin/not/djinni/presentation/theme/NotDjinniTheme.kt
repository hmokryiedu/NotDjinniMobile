package not.djinni.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun NotDjinniTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalNotDjinniOffset provides NotDjinniOffset(),
        LocalNotDjinniTypography provides NotDjinniTypography(),
        LocalNotDjinniColor provides NotDjinniColor(),
        LocalNotDjinniShape provides NotDjinniTheme.shapes,
        content = content
    )
}

object NotDjinniTheme {

    val colors: NotDjinniColor
        @Composable
        @ReadOnlyComposable
        get() = LocalNotDjinniColor.current

    val shapes: NotDjinniShape
        @Composable
        @ReadOnlyComposable
        get() = LocalNotDjinniShape.current

    val offsets: NotDjinniOffset
        @Composable
        @ReadOnlyComposable
        get() = LocalNotDjinniOffset.current

    val typography: NotDjinniTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalNotDjinniTypography.current
}