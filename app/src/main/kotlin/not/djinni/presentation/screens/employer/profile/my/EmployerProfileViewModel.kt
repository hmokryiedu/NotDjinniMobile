package not.djinni.presentation.screens.employer.profile.my

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.asSharedFlow
import not.djinni.core.extension.mutableSideEffect
import not.djinni.domain.repository.AuthRepository
import not.djinni.domain.repository.EmployerRepository
import not.djinni.domain.repository.UserRepository
import not.djinni.presentation.core.StateViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class EmployerProfileViewModel(
    private val userRepository: UserRepository,
    private val employerRepository: EmployerRepository,
    private val authRepository: AuthRepository,
) : StateViewModel<EmployerProfileState>(EmployerProfileState()) {

    private val _sideEffect = mutableSideEffect<EmployerProfileSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        loadProfile()
    }

    fun sendAction(action: EmployerProfileAction) {
        when (action) {
            EmployerProfileAction.NavigateBack -> navigateBack()
            EmployerProfileAction.ShowRoleEditor -> updateState {
                copy(
                    isRoleDialogVisible = true,
                    editingRole = profile?.role.orEmpty(),
                )
            }
            EmployerProfileAction.DismissRoleEditor -> updateState { copy(isRoleDialogVisible = false) }
            is EmployerProfileAction.UpdateEditingRole -> updateState { copy(editingRole = action.value) }
            EmployerProfileAction.SaveRole -> saveRole()
            EmployerProfileAction.Logout -> logOut()
            EmployerProfileAction.Retry -> loadProfile()
        }
    }

    private fun saveRole() {
        val role = state.value.editingRole.trim()
        if (role.isBlank()) return
        launch {
            updateState { copy(isSavingRole = true) }
            val profile = employerRepository.updateProfileRole(role)
            updateState {
                copy(
                    profile = profile,
                    isRoleDialogVisible = false,
                    isSavingRole = false
                )
            }
        }
    }

    private fun logOut() {
        launch {
            authRepository.logOut()
            _sideEffect.emit(EmployerProfileSideEffect.NavigateToAuth)
        }
    }

    private fun navigateBack() {
        launch {
            _sideEffect.emit(EmployerProfileSideEffect.NavigateBack)
        }
    }

    private fun loadProfile() {
        launch {
            updateState { copy(isLoading = true, hasError = false) }
            coroutineScope {
                val userDeferred = async { userRepository.getUser() }
                val profileDeferred = async { employerRepository.getProfile() }
                val user = userDeferred.await()
                val profile = profileDeferred.await()
                if (user == null || profile == null) {
                    updateState { copy(isLoading = false, hasError = true) }
                } else {
                    updateState {
                        copy(
                            user = user,
                            profile = profile,
                            isLoading = false,
                            hasError = false
                        )
                    }
                }
            }
        }
    }
}
