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

internal val ZoomIcon: ImageVector
    get() {
        if (_ZoomIcon != null) {
            return _ZoomIcon!!
        }
        _ZoomIcon = ImageVector.Builder(
            name = "ZoomIcon",
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
                moveTo(64.71f, 64.71f)
                lineTo(79.61f, 79.61f)
                moveTo(72.22f, 46.61f)
                curveTo(72.22f, 60.754f, 60.754f, 72.22f, 46.61f, 72.22f)
                curveTo(32.466f, 72.22f, 21f, 60.754f, 21f, 46.61f)
                curveTo(21f, 32.466f, 32.466f, 21f, 46.61f, 21f)
                curveTo(60.754f, 21f, 72.22f, 32.466f, 72.22f, 46.61f)
                close()
            }
        }.build()
        return _ZoomIcon!!
    }

private var _ZoomIcon: ImageVector? = null

@Preview
@Composable
private fun ZoomIconPreview() {
    Icon(
        imageVector = ZoomIcon,
        contentDescription = "ZoomIcon",
        tint = Color.Unspecified
    )
}