package not.djinni.presentation.core.components.base.model

import androidx.compose.runtime.Immutable

@Immutable
class SnackBarData(
    val message: TextData,
    val duration: Duration = Duration.SHORT,
) {
    enum class Duration(
        val animationDuration: Int = 500,
        val onScreenDuration: Int,
    ) {
        SHORT(onScreenDuration = 700),
        LONG(onScreenDuration = 1500)
    }
}