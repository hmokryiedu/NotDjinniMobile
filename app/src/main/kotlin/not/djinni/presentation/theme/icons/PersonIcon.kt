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

internal val PersonIcon: ImageVector
    get() {
        if (_PersonIcon != null) {
            return _PersonIcon!!
        }
        _PersonIcon = ImageVector.Builder(
            name = "PersonIcon",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = null,
                stroke = SolidColor(Color(0xFF1D1D1B)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(12f, 11f)
                curveTo(14.2091f, 11f, 16f, 9.2091f, 16f, 7f)
                curveTo(16f, 4.7909f, 14.2091f, 3f, 12f, 3f)
                curveTo(9.7909f, 3f, 8f, 4.7909f, 8f, 7f)
                curveTo(8f, 9.2091f, 9.7909f, 11f, 12f, 11f)
                close()
            }
            path(
                fill = null,
                stroke = SolidColor(Color(0xFF1D1D1B)),
                strokeLineWidth = 2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(6f, 21f)
                verticalLineTo(19f)
                curveTo(6f, 17.9391f, 6.4214f, 16.9217f, 7.1716f, 16.1716f)
                curveTo(7.9217f, 15.4214f, 8.9391f, 15f, 10f, 15f)
                horizontalLineTo(14f)
                curveTo(15.0609f, 15f, 16.0783f, 15.4214f, 16.8284f, 16.1716f)
                curveTo(17.5786f, 16.9217f, 18f, 17.9391f, 18f, 19f)
                verticalLineTo(21f)
            }
        }.build()
        return _PersonIcon!!
    }

private var _PersonIcon: ImageVector? = null

@Preview
@Composable
private fun PersonIconPreview() {
    Icon(
        imageVector = PersonIcon,
        contentDescription = "PersonIcon",
        tint = Color.Unspecified
    )
}
