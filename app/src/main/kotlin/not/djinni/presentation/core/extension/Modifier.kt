package not.djinni.presentation.core.extension

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import not.djinni.presentation.core.DeviceSizeType

private var lastClickTime = 0L
private const val CLICK_INTERVAL = 500L

inline fun Modifier.applyIf(condition: Boolean, modifier: Modifier.() -> Modifier): Modifier {
    return if (condition) then(modifier(Modifier)) else this
}

inline fun Modifier.applyWhen(modifier: Modifier.() -> Modifier): Modifier {
    return then(modifier(Modifier))
}

fun Modifier.drawCustomIndicatorLine(
    indicatorBorder: BorderStroke,
    indicatorPadding: Dp = 0.dp
): Modifier {

    val strokeWidthDp = indicatorBorder.width
    return drawWithContent {
        drawContent()
        if (strokeWidthDp == Dp.Hairline) return@drawWithContent
        val strokeWidth = strokeWidthDp.value * density
        val y = size.height - strokeWidth / 2
        drawLine(
            indicatorBorder.brush,
            Offset((indicatorPadding).toPx(), y),
            Offset(size.width - indicatorPadding.toPx(), y),
            strokeWidth
        )
    }
}

fun Modifier.noRippleClickable(onClick: () -> Unit) = composed {
    this.then(
        Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        )
    )
}

inline fun Modifier.whenDeviceSize(
    deviceSizeType: DeviceSizeType,
    modifier: Modifier.(DeviceSizeType) -> Modifier
): Modifier {
    return then(Modifier.modifier(deviceSizeType))
}

fun performWithTimeout(
    action: () -> Unit,
) = performWithTimeout(
    action = action,
    interval = CLICK_INTERVAL
)

fun performWithTimeout(
    action: () -> Unit,
    interval: Long = CLICK_INTERVAL,
) {
    require(CLICK_INTERVAL <= interval)
    val now = System.currentTimeMillis()
    if (now - lastClickTime > interval) {
        lastClickTime = now
        action()
    }
}

@Suppress("LongParameterList")
fun Modifier.clickableSingle(
    interactionSource: MutableInteractionSource,
    indication: Indication?,
    interval: Long = CLICK_INTERVAL,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit,
): Modifier {
    return this then Modifier.clickable(
        interactionSource = interactionSource,
        indication = indication,
        enabled = enabled,
        onClickLabel = onClickLabel,
        role = role,
        onClick = {
            performWithTimeout(
                action = onClick,
                interval = interval
            )
        }
    )
}

fun Modifier.clickableNoRipple(
    enabled: Boolean = true,
    interval: Long = CLICK_INTERVAL,
    onClick: () -> Unit,
) = composed {
    clickableSingle(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        interval = interval,
        enabled = enabled,
        onClick = onClick
    )
}

fun Modifier.clickableNoRippleWithoutInterval(
    enabled: Boolean = true,
    onClick: () -> Unit,
) = composed {
    clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        enabled = enabled,
        onClick = onClick
    )
}