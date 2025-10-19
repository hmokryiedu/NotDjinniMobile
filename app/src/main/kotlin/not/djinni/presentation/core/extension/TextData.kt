package not.djinni.presentation.core.extension

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import not.djinni.presentation.core.components.base.model.TextData

fun String?.toTextData(): TextData {
    return TextData.Text(value = this ?: "")
}

fun @receiver:StringRes Int.toTextData(): TextData {
    return TextData.Resource(resId = this)
}

fun AnnotatedString.toTextData(): TextData {
    return TextData.Annotated(annotatedString = this)
}

@Composable
fun TextData.asRawString(): String {
    return when (this) {
        is TextData.Text -> value
        is TextData.Resource -> stringResource(resId)
        is TextData.Annotated -> annotatedString.text
    }
}