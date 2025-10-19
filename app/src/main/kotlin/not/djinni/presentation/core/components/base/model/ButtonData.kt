package not.djinni.presentation.core.components.base.model

import androidx.compose.runtime.Immutable
import not.djinni.presentation.core.components.base.model.ImageData
import not.djinni.presentation.core.components.base.model.TextData

@Immutable
data class ButtonData(
    val text: TextData,
    val enabled: Boolean = true,
    val startImage: ImageData? = null,
    val endImage: ImageData? = null,
    val isLoading: Boolean = false,
)
