package not.djinni.presentation.screens.auth.role

import kotlinx.coroutines.flow.asSharedFlow
import not.djinni.core.extension.mutableSideEffect
import not.djinni.model.role.Role
import not.djinni.presentation.core.StateViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ChooseRoleViewModel : StateViewModel<ChooseRoleState>(ChooseRoleState()) {

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
        launch {
            val role = state.value.selectedRole ?: return@launch
            _sideEffect.emit(ChooseRoleSideEffect.NavigateMain(role))
        }
    }
}
