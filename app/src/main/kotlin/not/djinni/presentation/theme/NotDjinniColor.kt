package not.djinni.presentation.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class NotDjinniColor(
    val primary: Color = Color(0xFFEC6560),
    val onPrimaryContainer: Color = Color(0xFF000000),
    val secondary: Color = Color(0xFF484849),
    val tertiary: Color = Color(0xFF484849),
    val background: Color = Color(0xFFF2F2F7),
    val onBackground: Color = Color(0xFF000000),
    val surface: Color = Color(0xFFF2F2F7),
    val onSurface: Color = Color(0xFF000000),
    val surfaceVariant: Color = Color(0xFFFFFFFF),
    val onSurfaceVariant: Color = Color(0xFFDCDCDC),
    val surfaceContainer: Color = Color(0xFFFFFFFF),
    val surfaceContainerHigh: Color = Color(0xFFC7C7CC),
    val surfaceContainerHighInverted: Color = Color(0xFF484849),
    val surfaceTertiary: Color = Color(0xFFE5E5EA),
    val actionColor: Color = Color(0xFF8E8E93),
    val dividerColor: Color = Color(0xFF79747E),
    val caption: Color = Color(0xFF727476),
    val blue: Color = Color(0xFF467FE7),
    val forcedBlack: Color = Color(0xFF0E0E10),
    val surfaceSecondaryYellow: Color = Color(0xFFF2C84A),
    val surfaceSecondaryOrange: Color = Color(0xFFF2964A),
    val sheetContainerColor: Color = Color(0xFFF4F5F7),
    val surfaceQuaternary: Color = Color(0xFFE5E5EA),
    val backgroundVariant: Color = Color(0xFFF7F8FA),
    val onBackgroundVariant: Color = Color(0xFF05162B),
    val ocean: Color = Color(0xFF0C7AFF),
)

val darkVersion: NotDjinniColor
    get() = NotDjinniColor(
        primary = Color(0xFFEC6560),
        onPrimaryContainer = Color(0xFFFFFFFF),
        secondary = Color(0xFFE5E5EA),
        tertiary = Color(0xFFC7C7CC),
        background = Color(0xFF1C1C1D),
        onBackground = Color(0xFFFFFFFF),
        surface = Color(0xFF272729),
        onSurface = Color(0xFFFFFFFF),
        surfaceVariant = Color(0xFF272729),
        onSurfaceVariant = Color(0xFF4B4B33),
        surfaceContainer = Color(0xFF272729),
        surfaceContainerHigh = Color(0xFF484849),
        surfaceContainerHighInverted = Color(0xFFC7C7CC),
        surfaceTertiary = Color(0xFF272729),
        sheetContainerColor = Color(0xFF1C1C1D),
        surfaceQuaternary = Color(0xFF484849),
        backgroundVariant = Color(0xFF1C1C1D),
        onBackgroundVariant = Color(0xFFFFFFFF),
    )

internal val LocalNotDjinniColor = staticCompositionLocalOf {
    NotDjinniColor()
}