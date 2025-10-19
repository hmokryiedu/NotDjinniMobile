package not.djinni.presentation.core.extension

import androidx.annotation.StringRes
import not.djinni.presentation.core.components.base.model.SnackBarData
import not.djinni.presentation.core.components.base.model.TextData

fun String.toSnackBarData(): SnackBarData {
    return SnackBarData(message = this.toTextData())
}

fun TextData.toSnackBarData(): SnackBarData {
    return SnackBarData(message = this)
}

fun @receiver:StringRes Int.toSnackBarData(
    duration: SnackBarData.Duration = SnackBarData.Duration.SHORT
): SnackBarData {
    return SnackBarData(
        message = TextData.Resource(resId = this),
        duration = duration
    )
}
