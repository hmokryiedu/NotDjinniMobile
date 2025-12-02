package not.djinni.presentation.screens.auth.role

import kotlinx.coroutines.flow.asSharedFlow
import not.djinni.core.extension.mutableSideEffect
import not.djinni.datastore.session.SessionDataStore
import not.djinni.domain.repository.EmployerRepository
import not.djinni.domain.repository.SeekerRepository
import not.djinni.model.role.Role
import not.djinni.presentation.core.StateViewModel
import not.djinni.presentation.screens.auth.role.ChooseRoleSideEffect.NavigateEmployerCreateProfile
import not.djinni.presentation.screens.auth.role.ChooseRoleSideEffect.NavigateEmployerMain
import not.djinni.presentation.screens.auth.role.ChooseRoleSideEffect.NavigateSeekerCreateProfile
import not.djinni.presentation.screens.auth.role.ChooseRoleSideEffect.NavigateSeekerMain
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ChooseRoleViewModel(
    private val seekerRepository: SeekerRepository,
    private val employerRepository: EmployerRepository,
    private val sessionDataStore: SessionDataStore,
) : StateViewModel<ChooseRoleState>(ChooseRoleState()) {

    private val _sideEffect = mutableSideEffect<ChooseRoleSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun sendAction(action: ChooseRoleAction) {
        when (action) {
            is ChooseRoleAction.SelectRole -> selectRole(action.role)
            ChooseRoleAction.ProceedToMain -> proceedToMain()
        }
    }

    fun selectRole(role: Role) {
        updateState { copy(selectedRole = role) }
    }

    fun proceedToMain() {
        launch(loadingEnabled = true) {
            val role = state.value.selectedRole ?: return@launch
            sessionDataStore.setCurrentRole(role)
            val event = when (role) {
                Role.SEEKER -> seekerRepository.getProfile()
                    ?.let { NavigateSeekerMain }
                    ?: NavigateSeekerCreateProfile

                Role.EMPLOYER -> employerRepository.getProfile()
                    ?.let { NavigateEmployerMain }
                    ?: NavigateEmployerCreateProfile
            }
            _sideEffect.tryEmit(event)
        }
    }
}
