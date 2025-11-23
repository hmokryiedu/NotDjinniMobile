package not.djinni.presentation.screens.auth.role

import not.djinni.model.role.Role

internal sealed interface ChooseRoleSideEffect {
    data class NavigateMain(val role: Role) : ChooseRoleSideEffect
}
