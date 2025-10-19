package not.djinni.presentation.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import not.djinni.R
import not.djinni.presentation.core.DeviceSizeType

private val eUkraineFontFamily = FontFamily(
    Font(
        resId = R.font.e_ukraine_ultra_light,
        weight = FontWeight.Thin
    ),
    Font(
        resId = R.font.e_ukraine_light,
        weight = FontWeight.Light
    ),
    Font(
        resId = R.font.e_ukraine_regular,
        weight = FontWeight.Normal
    ),
    Font(
        resId = R.font.e_ukraine_medium,
        weight = FontWeight.Medium,
    ),
    Font(
        resId = R.font.e_ukraine_bold,
        weight = FontWeight.Bold
    ),
)

data class NotDjinniTypography(
    val title1: TextStyle = TextStyle(
        fontFamily = eUkraineFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 36.sp,
        lineHeight = 42.sp,
    ),
    val title1Bold: TextStyle = TextStyle(
        fontFamily = eUkraineFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
    ),
    val title2: TextStyle = TextStyle(
        fontFamily = eUkraineFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.sp,
    ),
    val title3: TextStyle = TextStyle(
        fontFamily = eUkraineFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
    ),
    val title4: TextStyle = TextStyle(
        fontFamily = eUkraineFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
    ),
    val title: TextStyle = TextStyle(
        fontFamily = eUkraineFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    ),
    val titleBold: TextStyle = title.copy(
        fontWeight = FontWeight.Bold
    ),
    val body1: TextStyle = TextStyle(
        fontFamily = eUkraineFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 17.sp,
        lineHeight = 24.sp,
    ),
    val body1Bold: TextStyle = TextStyle(
        fontFamily = eUkraineFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 17.sp,
        lineHeight = 22.sp,
    ),
    val body2: TextStyle = TextStyle(
        fontFamily = eUkraineFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 22.sp,
    ),
    val body3: TextStyle = TextStyle(
        fontFamily = eUkraineFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    val caption1: TextStyle = TextStyle(
        fontFamily = eUkraineFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 16.sp,
    ),
    val caption2: TextStyle = TextStyle(
        fontFamily = eUkraineFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
    val footnote: TextStyle = TextStyle(
        fontFamily = eUkraineFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 10.sp,
    ),

    val body1Semibold: TextStyle = TextStyle(
        fontFamily = eUkraineFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.sp
    ),
    val caption2Bold: TextStyle = caption2.copy(
        fontWeight = FontWeight.Bold
    ),
)

internal val mediumVoiceRecorderTypography by lazy { buildTypography(DeviceSizeType.MEDIUM.multiplier) }
internal val smallVoiceRecorderTypography by lazy { buildTypography(DeviceSizeType.SMALL.multiplier) }
internal val extraSmallVoiceRecorderTypography by lazy { buildTypography(DeviceSizeType.EXTRA_SMALL.multiplier) }

internal val LocalNotDjinniTypography = staticCompositionLocalOf {
    NotDjinniTypography()
}

private fun buildTypography(scale: Float): NotDjinniTypography {
    return NotDjinniTypography().run {
        copy(
            title1 = title1.copy(
                fontSize = title1.fontSize * scale,
                lineHeight = title1.lineHeight * scale
            ),
            title1Bold = title1Bold.copy(
                fontSize = title1Bold.fontSize * scale,
                lineHeight = title1Bold.lineHeight * scale
            ),
            title2 = title2.copy(
                fontSize = title2.fontSize * scale,
                lineHeight = title2.lineHeight * scale
            ),
            title3 = title3.copy(
                fontSize = title3.fontSize * scale,
                lineHeight = title3.lineHeight * scale
            ),
            title4 = title4.copy(
                fontSize = title4.fontSize * scale,
                lineHeight = title4.lineHeight * scale
            ),
            title = title.copy(fontSize = title.fontSize * scale),
            body1 = body1.copy(
                fontSize = body1.fontSize * scale,
                lineHeight = body1.lineHeight * scale
            ),
            body1Bold = body1Bold.copy(
                fontSize = body1Bold.fontSize * scale,
                lineHeight = body1Bold.lineHeight * scale
            ),
            body2 = body2.copy(
                fontSize = body2.fontSize * scale,
                lineHeight = body2.lineHeight * scale
            ),
            body3 = body3.copy(
                fontSize = body3.fontSize * scale,
                lineHeight = body3.lineHeight * scale
            ),
            caption1 = caption1.copy(
                fontSize = caption1.fontSize * scale,
                lineHeight = caption1.lineHeight * scale
            ),
            caption2 = caption2.copy(
                fontSize = caption2.fontSize * scale,
                lineHeight = caption2.lineHeight * scale
            ),
            footnote = footnote.copy(fontSize = footnote.fontSize * scale)
        )
    }
}
