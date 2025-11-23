package not.djinni.domain.usecase.auth

import not.djinni.domain.repository.AuthRepository
import not.djinni.domain.usecase.core.UseCase
import org.koin.core.annotation.Factory

@Factory
class CheckIsLoggedInUseCase(
    private val authRepository: AuthRepository
) : UseCase<Boolean> {

    override suspend fun invoke() = runCatching {
        authRepository.getIsLoggedIn()
    }.getOrDefault(false)
}