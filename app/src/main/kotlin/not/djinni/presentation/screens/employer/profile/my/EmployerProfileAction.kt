package not.djinni.presentation.screens.employer.profile.my

internal sealed interface EmployerProfileAction {
    data object NavigateBack : EmployerProfileAction
    data object ShowRoleEditor : EmployerProfileAction
    data object ChooseRole : EmployerProfileAction
    data object DismissRoleEditor : EmployerProfileAction
    data class UpdateEditingRole(val value: String) : EmployerProfileAction
    data object SaveRole : EmployerProfileAction
    data object RequestLogout : EmployerProfileAction
    data object ConfirmLogout : EmployerProfileAction
    data object DismissLogoutConfirmation : EmployerProfileAction
    data object Retry : EmployerProfileAction
}
