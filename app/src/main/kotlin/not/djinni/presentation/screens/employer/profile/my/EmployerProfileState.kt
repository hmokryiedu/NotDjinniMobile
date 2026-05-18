package not.djinni.presentation.screens.employer.profile.my

import androidx.compose.runtime.Immutable
import not.djinni.model.User
import not.djinni.model.employer.EmployerProfile

@Immutable
internal data class EmployerProfileState(
    val user: User? = null,
    val profile: EmployerProfile? = null,
    val isLoading: Boolean = true,
    val hasError: Boolean = false,
    val editingRole: String = "",
    val isRoleDialogVisible: Boolean = false,
    val isSavingRole: Boolean = false,
)
