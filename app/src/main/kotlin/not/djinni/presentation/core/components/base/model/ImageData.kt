package not.djinni.presentation.core.components.base.model

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
sealed class ImageData {

    data class Vector(val vector: ImageVector) : ImageData()
    data class Resource(@param:DrawableRes val resId: Int) : ImageData()
}
