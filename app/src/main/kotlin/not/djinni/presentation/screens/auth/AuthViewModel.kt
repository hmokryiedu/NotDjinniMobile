package not.djinni.presentation.screens.auth

import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import not.djinni.core.extension.mutableSideEffect
import not.djinni.domain.usecase.auth.SignInUseCase
import not.djinni.domain.usecase.auth.SignUpUseCase
import not.djinni.presentation.core.StateViewModel
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.screens.auth.AuthState.AuthType.*
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class AuthViewModel(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
) : StateViewModel<AuthState>(AuthState()) {

    private val _sideEffect = mutableSideEffect<AuthSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun onLogin(email: String, password: String) {
        launch {
            when (mutableState.value.type) {
                SIGN_IN -> signInUseCase(SignInUseCase.Params(email = email, password = password))
                SIGN_UP -> signUpUseCase(SignUpUseCase.Params(email = email, password = password))
            }.onSuccess {
                _sideEffect.emit(AuthSideEffect.NavigateHome)
            }.onFailure { throwable ->
                mutableState.update { it.copy(errorMessage = throwable.message.toTextData()) }
            }
        }
    }

    fun onAuthTypeChange() {
        val type = if (mutableState.value.type == SIGN_IN) SIGN_UP else SIGN_IN
        mutableState.update { it.copy(type = type, errorMessage = null) }
    }
}