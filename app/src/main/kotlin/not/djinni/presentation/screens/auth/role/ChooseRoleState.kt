package not.djinni.presentation.screens.auth.role

import androidx.compose.runtime.Stable
import not.djinni.model.role.Role

@Stable
internal data class ChooseRoleState(
    val selectedRole: Role? = null,
)