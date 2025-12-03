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
    val success: Color = Color(0xFF4CAF50),
    val highlightedContainer: Color = Color(0xFF282828),
    val applicationStatusApplied: Color = Color(0xFF2196F3),
    val applicationStatusReviewing: Color = Color(0xFFFFC107),
    val applicationStatusInterviewing: Color = Color(0xFF9C27B0),
    val applicationStatusOffered: Color = Color(0xFF4CAF50),
    val applicationStatusAccepted: Color = Color(0xFF00BCD4),
    val applicationStatusRejected: Color = Color(0xFFF44336),
)

internal val LocalNotDjinniColor = staticCompositionLocalOf {
    NotDjinniColor()
}