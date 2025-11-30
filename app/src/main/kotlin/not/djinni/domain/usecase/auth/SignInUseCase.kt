package not.djinni.domain.usecase.auth

import not.djinni.domain.repository.AuthRepository
import not.djinni.domain.usecase.core.UseCaseWithParams
import not.djinni.domain.validator.AuthDataValidator
import org.koin.core.annotation.Factory

@Factory
class SignInUseCase(
    private val authRepository: AuthRepository,
    private val authDataValidator: AuthDataValidator,
) : UseCaseWithParams<Result<Unit>, SignInUseCase.Params> {

    override suspend fun invoke(params: Params) = runCatching {
        if (!authDataValidator.validateEmail(params.email)) error("Email is not valid")
        if (!authDataValidator.validatePassword(params.password)) error("Password is not valid")
        authRepository.signIn(email = params.email, password = params.password)
    }

    data class Params(
        val email: String,
        val password: String
    )
}