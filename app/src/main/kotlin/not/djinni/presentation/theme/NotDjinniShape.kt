package not.djinni.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

data class NotDjinniShape(
    val micro: RoundedCornerShape = RoundedCornerShape(4.dp),
    val tiny: RoundedCornerShape = RoundedCornerShape(6.dp),
    val small: RoundedCornerShape = RoundedCornerShape(8.dp),
    val little: RoundedCornerShape = RoundedCornerShape(10.dp),
    val medium: RoundedCornerShape = RoundedCornerShape(12.dp),
    val regular: RoundedCornerShape = RoundedCornerShape(14.dp),
    val large: RoundedCornerShape = RoundedCornerShape(16.dp),
    val intermediate: RoundedCornerShape = RoundedCornerShape(18.dp),
    val average: RoundedCornerShape = RoundedCornerShape(20.dp),
    val significant: RoundedCornerShape = RoundedCornerShape(22.dp),
    val huge: RoundedCornerShape = RoundedCornerShape(24.dp),
    val great: RoundedCornerShape = RoundedCornerShape(28.dp),
    val enormous: RoundedCornerShape = RoundedCornerShape(30.dp),
    val massive: RoundedCornerShape = RoundedCornerShape(34.dp),
)

internal val LocalNotDjinniShape = staticCompositionLocalOf { NotDjinniShape() }