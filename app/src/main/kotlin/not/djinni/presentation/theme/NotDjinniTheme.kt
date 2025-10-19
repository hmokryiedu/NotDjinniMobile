package not.djinni.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import not.djinni.presentation.theme.LocalNotDjinniTypography
import not.djinni.presentation.theme.NotDjinniTypography

@Composable
fun NotDjinniTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalNotDjinniOffset provides NotDjinniOffset(),
        LocalNotDjinniTypography provides NotDjinniTypography(),
        LocalNotDjinniColor provides if (darkTheme) darkVersion else NotDjinniColor(),
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