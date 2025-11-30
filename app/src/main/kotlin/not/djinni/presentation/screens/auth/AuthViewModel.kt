package not.djinni.presentation.screens.auth

import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import not.djinni.core.extension.mutableSideEffect
import not.djinni.core.logging.error
import not.djinni.domain.usecase.auth.SignInUseCase
import not.djinni.domain.usecase.auth.SignUpUseCase
import not.djinni.presentation.core.StateViewModel
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.screens.auth.AuthState.AuthType.SIGN_IN
import not.djinni.presentation.screens.auth.AuthState.AuthType.SIGN_UP
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class AuthViewModel(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
) : StateViewModel<AuthState>(AuthState()) {

    private val _sideEffect = mutableSideEffect<AuthSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun sendAction(action: AuthAction) {
        when (action) {
            AuthAction.SwitchAuthType -> switchAuthType()
            is AuthAction.AuthButtonClicked -> when (state.value.type) {
                SIGN_IN -> signIn(
                    email = action.state.email,
                    password = action.state.password
                )

                SIGN_UP -> signUp(
                    name = action.state.name,
                    email = action.state.email,
                    password = action.state.password
                )
            }
        }
    }

    private fun signIn(email: String, password: String) {
        launch {
            signInUseCase(SignInUseCase.Params(email = email, password = password))
                .onSuccess { _sideEffect.emit(AuthSideEffect.NavigateNext) }
                .onFailure { throwable ->
                    mutableState.update { it.copy(errorMessage = throwable.message.toTextData()) }
                    error("AuthViewModel", throwable) { "Error in signIn: ${throwable.message}" }
                }
        }
    }

    private fun signUp(name: String, email: String, password: String) {
        launch {
            signUpUseCase(SignUpUseCase.Params(name = name, email = email, password = password))
                .onSuccess { _sideEffect.emit(AuthSideEffect.NavigateNext) }
                .onFailure { throwable ->
                    mutableState.update { it.copy(errorMessage = throwable.message.toTextData()) }
                    error("AuthViewModel", throwable) { "Error in signUp: ${throwable.message}" }
                }
        }
    }

    private fun switchAuthType() {
        val type = if (mutableState.value.type == SIGN_IN) SIGN_UP else SIGN_IN
        mutableState.update { it.copy(type = type, errorMessage = null) }
    }
}