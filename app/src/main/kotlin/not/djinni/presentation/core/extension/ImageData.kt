package not.djinni.presentation.core.extension

import androidx.compose.ui.graphics.vector.ImageVector
import not.djinni.presentation.core.components.base.model.ImageData

fun ImageVector.toImageData(): ImageData {
    return ImageData.Vector(this)
}