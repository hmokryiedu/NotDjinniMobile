package not.djinni.domain.usecase.auth

import not.djinni.domain.repository.AuthRepository
import not.djinni.domain.usecase.core.UseCaseWithParams
import not.djinni.model.User
import org.koin.core.annotation.Factory

@Factory
class SignInUseCase(
    private val authRepository: AuthRepository
) : UseCaseWithParams<Result<User>, SignInUseCase.Params> {

    override suspend fun invoke(params: Params) = runCatching {
        authRepository.signIn(email = params.email, password = params.password)
    }

    data class Params(
        val email: String,
        val password: String
    )
}