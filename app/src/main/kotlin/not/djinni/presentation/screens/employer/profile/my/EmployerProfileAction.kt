package not.djinni.presentation.screens.employer.profile.my

internal sealed interface EmployerProfileAction {
    data object ShowRoleEditor : EmployerProfileAction
    data object DismissRoleEditor : EmployerProfileAction
    data class UpdateEditingRole(val value: String) : EmployerProfileAction
    data object SaveRole : EmployerProfileAction
    data object Logout : EmployerProfileAction
    data object Retry : EmployerProfileAction
}
