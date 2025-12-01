package not.djinni.presentation.theme.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

internal val PlusIcon: ImageVector
    get() {
        if (_PlusIcon != null) {
            return _PlusIcon!!
        }
        _PlusIcon = ImageVector.Builder(
            name = "PlusIcon",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000))
            ) {
                moveTo(20.5f, 11f)
                lineTo(15f, 11f)
                curveTo(14.4477f, 11f, 14f, 10.5523f, 14f, 10f)
                lineTo(14f, 4.5f)
                curveTo(14f, 3.6716f, 13.3284f, 3f, 12.5f, 3f)
                curveTo(11.6716f, 3f, 11f, 3.6716f, 11f, 4.5f)
                lineTo(11f, 10f)
                curveTo(11f, 10.5523f, 10.5523f, 11f, 10f, 11f)
                lineTo(4.5f, 11f)
                curveTo(3.6716f, 11f, 3f, 11.6716f, 3f, 12.5f)
                curveTo(3f, 13.3284f, 3.6716f, 14f, 4.5f, 14f)
                lineTo(10f, 14f)
                curveTo(10.5523f, 14f, 11f, 14.4477f, 11f, 15f)
                lineTo(11f, 20.5f)
                curveTo(11f, 21.3284f, 11.6716f, 22f, 12.5f, 22f)
                curveTo(13.3284f, 22f, 14f, 21.3284f, 14f, 20.5f)
                lineTo(14f, 15f)
                curveTo(14f, 14.4477f, 14.4477f, 14f, 15f, 14f)
                lineTo(20.5f, 14f)
                curveTo(21.3284f, 14f, 22f, 13.3284f, 22f, 12.5f)
                curveTo(22f, 11.6716f, 21.3284f, 11f, 20.5f, 11f)
                close()
            }
        }.build()
        return _PlusIcon!!
    }

private var _PlusIcon: ImageVector? = null

@Preview
@Composable
private fun PlusIconPreview() {
    Icon(
        imageVector = PlusIcon,
        contentDescription = "PlusIcon",
        tint = Color.Unspecified
    )
}