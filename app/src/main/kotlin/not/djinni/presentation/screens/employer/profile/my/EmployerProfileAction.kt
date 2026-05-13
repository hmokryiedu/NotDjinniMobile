package not.djinni.presentation.screens.employer.profile.my

internal sealed interface EmployerProfileAction {
    data object ChangeRole : EmployerProfileAction
    data object Logout : EmployerProfileAction
    data object Retry : EmployerProfileAction
}
