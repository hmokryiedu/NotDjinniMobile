package not.djinni.presentation.theme.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

internal val CaseIcon: ImageVector
    get() {
        if (_CaseIcon != null) {
            return _CaseIcon!!
        }
        _CaseIcon = ImageVector.Builder(
            name = "CaseIcon",
            defaultWidth = 100.dp,
            defaultHeight = 100.dp,
            viewportWidth = 100f,
            viewportHeight = 100f
        ).apply {
            path(
                fill = null,
                stroke = SolidColor(Color(0xFF1D1D1B)),
                strokeLineWidth = 5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(15.003f, 70.18f)
                verticalLineTo(75.43f)
                curveTo(14.9517f, 77.0987f, 15.5639f, 78.7197f, 16.7053f, 79.938f)
                curveTo(17.8468f, 81.1563f, 19.4245f, 81.8726f, 21.0929f, 81.93f)
                horizontalLineTo(79.703f)
                curveTo(81.3715f, 81.8726f, 82.9492f, 81.1563f, 84.0906f, 79.938f)
                curveTo(85.2321f, 78.7197f, 85.8442f, 77.0987f, 85.793f, 75.43f)
                verticalLineTo(41.49f)
                curveTo(85.8442f, 39.8213f, 85.2321f, 38.2003f, 84.0906f, 36.982f)
                curveTo(82.9492f, 35.7637f, 81.3715f, 35.0474f, 79.703f, 34.99f)
                horizontalLineTo(21.0929f)
                curveTo(19.4245f, 35.0474f, 17.8468f, 35.7637f, 16.7053f, 36.982f)
                curveTo(15.5639f, 38.2003f, 14.9517f, 39.8213f, 15.003f, 41.49f)
                verticalLineTo(70f)
                moveTo(15.003f, 52.68f)
                horizontalLineTo(41.503f)
                moveTo(59.293f, 52.68f)
                horizontalLineTo(85.793f)
                moveTo(32.473f, 34.99f)
                verticalLineTo(24.79f)
                curveTo(32.473f, 23.2544f, 33.083f, 21.7817f, 34.1688f, 20.6959f)
                curveTo(35.2547f, 19.61f, 36.7274f, 19f, 38.263f, 19f)
                horizontalLineTo(62.533f)
                curveTo(64.0695f, 19f, 65.5433f, 19.6097f, 66.6307f, 20.6953f)
                curveTo(67.7181f, 21.7808f, 68.3304f, 23.2535f, 68.333f, 24.79f)
                verticalLineTo(34.99f)
                moveTo(44.003f, 47.16f)
                horizontalLineTo(56.803f)
                curveTo(58.1837f, 47.16f, 59.303f, 48.2793f, 59.303f, 49.66f)
                verticalLineTo(55.71f)
                curveTo(59.303f, 57.0907f, 58.1837f, 58.21f, 56.803f, 58.21f)
                horizontalLineTo(44.003f)
                curveTo(42.6223f, 58.21f, 41.503f, 57.0907f, 41.503f, 55.71f)
                verticalLineTo(49.66f)
                curveTo(41.503f, 48.2793f, 42.6223f, 47.16f, 44.003f, 47.16f)
                close()
            }
        }.build()
        return _CaseIcon!!
    }

private var _CaseIcon: ImageVector? = null

@Preview
@Composable
private fun CaseIconPreview() {
    Icon(
        imageVector = CaseIcon,
        contentDescription = "CaseIcon",
        tint = Color.Unspecified
    )
}