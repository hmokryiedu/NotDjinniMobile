package not.djinni.presentation.screens.employer.profile.my

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.asSharedFlow
import not.djinni.core.extension.mutableSideEffect
import not.djinni.domain.repository.EmployerRepository
import not.djinni.domain.repository.UserRepository
import not.djinni.presentation.core.StateViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class EmployerProfileViewModel(
    private val userRepository: UserRepository,
    private val employerRepository: EmployerRepository,
) : StateViewModel<EmployerProfileState>(EmployerProfileState()) {

    private val _sideEffect = mutableSideEffect<EmployerProfileSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        loadProfile()
    }

    fun sendAction(action: EmployerProfileAction) {
        when (action) {
            EmployerProfileAction.ChangeRole -> {
                _sideEffect.tryEmit(EmployerProfileSideEffect.NavigateToChooseRole)
            }
            EmployerProfileAction.Retry -> loadProfile()
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
