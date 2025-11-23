package not.djinni.presentation.screens.auth.role

import kotlinx.coroutines.flow.asSharedFlow
import not.djinni.core.extension.mutableSideEffect
import not.djinni.domain.usecase.profile.GetEmployerProfileUseCase
import not.djinni.domain.usecase.profile.GetSeekerProfileUseCase
import not.djinni.model.role.Role
import not.djinni.presentation.core.StateViewModel
import not.djinni.presentation.screens.auth.role.ChooseRoleSideEffect.NavigateEmployerCreateProfile
import not.djinni.presentation.screens.auth.role.ChooseRoleSideEffect.NavigateEmployerMain
import not.djinni.presentation.screens.auth.role.ChooseRoleSideEffect.NavigateSeekerCreateProfile
import not.djinni.presentation.screens.auth.role.ChooseRoleSideEffect.NavigateSeekerMain
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ChooseRoleViewModel(
    private val getSeekerProfileUseCase: GetSeekerProfileUseCase,
    private val getEmployerProfileUseCase: GetEmployerProfileUseCase
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
            when (role) {
                Role.SEEKER -> {
                    val event = getSeekerProfileUseCase()
                        ?.let { NavigateSeekerCreateProfile }
                        ?: NavigateSeekerMain
                    _sideEffect.tryEmit(event)
                }

                Role.EMPLOYER -> {
                    val event = getEmployerProfileUseCase()
                        ?.let { NavigateEmployerCreateProfile }
                        ?: NavigateEmployerMain
                    _sideEffect.tryEmit(event)
                }
            }
        }
    }
}
