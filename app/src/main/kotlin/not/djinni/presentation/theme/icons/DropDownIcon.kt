package not.djinni.presentation.theme.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

internal val DropDownIcon: ImageVector
    get() {
        if (_DropDownIcon != null) {
            return _DropDownIcon!!
        }
        _DropDownIcon = ImageVector.Builder(
            name = "DropDownIcon",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000))
            ) {
                moveTo(12f, 15.4f)
                curveTo(11.867f, 15.4f, 11.742f, 15.379f, 11.625f, 15.337f)
                curveTo(11.508f, 15.296f, 11.4f, 15.225f, 11.3f, 15.125f)
                lineTo(6.7f, 10.525f)
                curveTo(6.517f, 10.342f, 6.421f, 10.113f, 6.412f, 9.837f)
                curveTo(6.404f, 9.563f, 6.5f, 9.333f, 6.7f, 9.15f)
                curveTo(6.883f, 8.95f, 7.113f, 8.85f, 7.387f, 8.85f)
                curveTo(7.663f, 8.85f, 7.9f, 8.95f, 8.1f, 9.15f)
                lineTo(12f, 13.05f)
                lineTo(15.925f, 9.125f)
                curveTo(16.108f, 8.942f, 16.333f, 8.846f, 16.6f, 8.837f)
                curveTo(16.867f, 8.829f, 17.1f, 8.925f, 17.3f, 9.125f)
                curveTo(17.5f, 9.308f, 17.6f, 9.538f, 17.6f, 9.812f)
                curveTo(17.6f, 10.088f, 17.5f, 10.325f, 17.3f, 10.525f)
                lineTo(12.7f, 15.125f)
                curveTo(12.6f, 15.225f, 12.492f, 15.296f, 12.375f, 15.337f)
                curveTo(12.258f, 15.379f, 12.133f, 15.4f, 12f, 15.4f)
                close()
            }
        }.build()
        return _DropDownIcon!!
    }

private var _DropDownIcon: ImageVector? = null

@Preview
@Composable
private fun DropDownIconPreview() {
    Icon(
        imageVector = DropDownIcon,
        contentDescription = "DropDownIcon",
        tint = Color.Unspecified
    )
}
