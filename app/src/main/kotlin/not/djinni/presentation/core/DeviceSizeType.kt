package not.djinni.presentation.core

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

enum class DeviceSizeType(val multiplier: Float = 1f) {
    EXTRA_SMALL(0.8f), SMALL(0.85f), MEDIUM(0.9f), DEFAULT, TABLET;

    val isCompact: Boolean
        get() = this == EXTRA_SMALL || this == SMALL
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun rememberDeviceSizeType(): State<DeviceSizeType> {
    val configuration = LocalConfiguration.current
    return remember(configuration.screenHeightDp) {
        derivedStateOf {
            val screenHeight = configuration.screenHeightDp.dp
            when {
                screenHeight < MAX_EXTRA_SMALL_DEVICE_HEIGHT.dp -> DeviceSizeType.EXTRA_SMALL
                screenHeight < MAX_SMALL_DEVICE_HEIGHT.dp -> DeviceSizeType.SMALL
                screenHeight < MAX_MEDIUM_DEVICE_HEIGHT.dp -> DeviceSizeType.MEDIUM
                screenHeight < MIN_TABLET_DEVICE_HEIGHT.dp -> DeviceSizeType.DEFAULT
                else -> DeviceSizeType.TABLET
            }
        }
    }
}

val LocalDeviceSizeType = staticCompositionLocalOf<DeviceSizeType> {
    error("No DeviceSizeType provided")
}

private const val MAX_EXTRA_SMALL_DEVICE_HEIGHT = 590
private const val MAX_SMALL_DEVICE_HEIGHT = 660
private const val MAX_MEDIUM_DEVICE_HEIGHT = 780
private const val MIN_TABLET_DEVICE_HEIGHT = 1100