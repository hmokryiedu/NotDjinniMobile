package not.djinni.domain.usecase.application

import not.djinni.domain.repository.ApplicationRepository
import not.djinni.domain.usecase.core.UseCaseWithParams
import org.koin.core.annotation.Factory

@Factory
class WithdrawApplicationUseCase(
    private val repository: ApplicationRepository,
) : UseCaseWithParams<Result<Unit>, Long> {
    override suspend fun invoke(params: Long): Result<Unit> {
        return repository.withdrawApplication(params)
    }
}
