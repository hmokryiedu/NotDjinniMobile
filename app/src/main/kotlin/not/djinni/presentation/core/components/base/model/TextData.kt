package not.djinni.presentation.core.components.base.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString

@Immutable
sealed class TextData {
    data class Text(val value: String) : TextData()
    data class Resource(@param:StringRes val resId: Int) : TextData()
    data class Annotated(val annotatedString: AnnotatedString) : TextData();

    fun isEmpty(): Boolean = when (this) {
        is Text -> value.isEmpty()
        is Resource -> false
        is Annotated -> annotatedString.isEmpty()
    }

    companion object {
        val Empty = Text("")
    }
}
