package not.djinni.presentation.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class NotDjinniColor(
    val primary: Color = Color.White,
    val onPrimary: Color = Color(0xFF1B1C1E),
    val background: Color = Color(0xFF1B1C1E),
    val onBackground: Color = Color.White,
    val surface: Color = Color(0xFF1B1C1E),
    val onSurface: Color = Color.White,
    val surfaceContainer: Color = Color(0xFF1B1C1E),
    val forcedBlack: Color = Color(0xFF1B1C1E),
    val error: Color = Color(0xFFF54927),
    val highlightedContainer: Color = Color(0xFF282828),
)

internal val LocalNotDjinniColor = staticCompositionLocalOf {
    NotDjinniColor()
}