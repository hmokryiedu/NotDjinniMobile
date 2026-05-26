package not.djinni.presentation.screens.auth.role

import not.djinni.model.role.Role

internal sealed interface ChooseRoleAction {
    data class ConfirmRole(val role: Role) : ChooseRoleAction
}
