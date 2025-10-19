package not.djinni.domain.usecase.auth

import not.djinni.domain.repository.AuthRepository
import not.djinni.domain.usecase.core.UseCaseWithParams
import org.koin.core.annotation.Factory

@Factory
class SignUpUseCase(
    private val authRepository: AuthRepository
) : UseCaseWithParams<Result<Unit>, SignUpUseCase.Params> {

    override suspend fun invoke(params: Params) = runCatching {
        authRepository.signUp(email = params.email, password = params.password)
    }

    data class Params(
        val email: String,
        val password: String
    )
}