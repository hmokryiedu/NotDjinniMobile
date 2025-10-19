package not.djinni.presentation.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class NotDjinniColor(
    val primary: Color = Color(0xFF1A1A1A),
    val onPrimaryContainer: Color = Color(0xFFFFFFFF),
    val secondary: Color = Color(0xFF2A2A2A),
    val tertiary: Color = Color(0xFF3A3A3A),
    val background: Color = Color(0xFF121212),
    val onBackground: Color = Color(0xFFFFFFFF),
    val surface: Color = Color(0xFF1E1E1E),
    val onSurface: Color = Color(0xFFFFFFFF),
    val surfaceVariant: Color = Color(0xFF2A2A2A),
    val onSurfaceVariant: Color = Color(0xFFB0B0B0),
    val surfaceContainer: Color = Color(0xFF252525),
    val surfaceContainerHigh: Color = Color(0xFF3A3A3A),
    val surfaceContainerHighInverted: Color = Color(0xFFE5E5E5),
    val surfaceTertiary: Color = Color(0xFF2A2A2A),
    val actionColor: Color = Color(0xFF8E8E93),
    val dividerColor: Color = Color(0xFF404040),
    val caption: Color = Color(0xFF888888),
    val blue: Color = Color(0xFF467FE7),
    val forcedBlack: Color = Color(0xFF000000),
    val surfaceSecondaryYellow: Color = Color(0xFFF2C84A),
    val surfaceSecondaryOrange: Color = Color(0xFFF2964A),
    val sheetContainerColor: Color = Color(0xFF1E1E1E),
    val surfaceQuaternary: Color = Color(0xFF2A2A2A),
    val backgroundVariant: Color = Color(0xFF1A1A1A),
    val onBackgroundVariant: Color = Color(0xFFFFFFFF),
    val ocean: Color = Color(0xFF0C7AFF),
)

internal val LocalNotDjinniColor = staticCompositionLocalOf {
    NotDjinniColor()
}