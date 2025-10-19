package not.djinni.presentation.core.extension

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.delete
import androidx.compose.ui.text.coerceIn

fun TextFieldState.replaceText(newText: String) {
    edit {
        delete(0, text.length)
        append(newText)
        this.selection = originalSelection.coerceIn(0, newText.length)
    }
}