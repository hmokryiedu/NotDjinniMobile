package not.djinni.presentation.screens.auth

import androidx.compose.runtime.Immutable
import not.djinni.R
import not.djinni.presentation.core.components.base.model.TextData
import not.djinni.presentation.core.extension.toTextData

@Immutable
internal data class AuthState(
    val type: AuthType = AuthType.SIGN_IN,
    val errorMessage: TextData? = null
) {
    enum class AuthType(
        val titleText: TextData,
        val switchText: TextData,
        val buttonText: TextData,
    ) {
        SIGN_IN(
            titleText = R.string.sign_in.toTextData(),
            switchText = R.string.switch_to_sign_up.toTextData(),
            buttonText = R.string.sign_in.toTextData(),
        ),
        SIGN_UP(
            titleText = R.string.sign_up.toTextData(),
            switchText = R.string.switch_to_sign_in.toTextData(),
            buttonText = R.string.sign_up.toTextData(),
        )
    }
}