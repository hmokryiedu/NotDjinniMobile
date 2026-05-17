package not.djinni.presentation.screens.auth

import not.djinni.presentation.screens.auth.model.FieldsState

internal sealed interface AuthAction {
    data object SwitchAuthType : AuthAction
    data object ViewOnlyClicked : AuthAction
    data class AuthButtonClicked(val state: FieldsState) : AuthAction
}
