package not.djinni.presentation.theme.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

internal val CheckIcon: ImageVector
    get() {
        if (_CheckIcon != null) {
            return _CheckIcon!!
        }
        _CheckIcon = ImageVector.Builder(
            name = "CheckIcon",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000))
            ) {
                moveTo(9f, 16.17f)
                lineTo(4.83f, 12f)
                lineTo(3.41f, 13.41f)
                lineTo(9f, 19f)
                lineTo(21f, 7f)
                lineTo(19.59f, 5.59f)
                lineTo(9f, 16.17f)
                close()
            }
        }.build()
        return _CheckIcon!!
    }

private var _CheckIcon: ImageVector? = null

@Preview
@Composable
private fun CheckIconPreview() {
    Icon(
        imageVector = CheckIcon,
        contentDescription = "CheckIcon",
        tint = Color.Unspecified
    )
}
