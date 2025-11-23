package not.djinni.presentation.screens.auth.role

import not.djinni.model.role.Role

internal sealed interface ChooseRoleAction {
    data class SelectRole(val role: Role) : ChooseRoleAction
    object ProceedToMain : ChooseRoleAction
}