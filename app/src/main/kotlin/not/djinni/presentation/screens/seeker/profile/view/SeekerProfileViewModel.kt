package not.djinni.presentation.screens.seeker.profile.view

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.asSharedFlow
import not.djinni.core.extension.mutableSideEffect
import not.djinni.domain.repository.AuthRepository
import not.djinni.domain.repository.SeekerRepository
import not.djinni.domain.repository.UserRepository
import not.djinni.presentation.core.StateViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class SeekerProfileViewModel(
    private val userRepository: UserRepository,
    private val seekerRepository: SeekerRepository,
    private val authRepository: AuthRepository,
) : StateViewModel<SeekerProfileState>(SeekerProfileState()) {

    private val _sideEffect = mutableSideEffect<SeekerProfileSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        loadProfile()
    }

    fun sendAction(action: SeekerProfileAction) {
        when (action) {
            SeekerProfileAction.ChangeRole -> {
                _sideEffect.tryEmit(SeekerProfileSideEffect.NavigateToChooseRole)
            }
            SeekerProfileAction.Logout -> logOut()
            SeekerProfileAction.Retry -> loadProfile()
        }
    }

    private fun logOut() {
        launch {
            authRepository.logOut()
            _sideEffect.emit(SeekerProfileSideEffect.NavigateToAuth)
        }
    }

    private fun loadProfile() {
        launch {
            updateState { copy(isLoading = true, hasError = false) }
            coroutineScope {
                val userDeferred = async { userRepository.getUser() }
                val profileDeferred = async { seekerRepository.getProfile() }
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
