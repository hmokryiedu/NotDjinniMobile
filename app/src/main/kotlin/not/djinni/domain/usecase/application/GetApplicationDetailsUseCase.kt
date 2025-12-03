package not.djinni.domain.usecase.application

import not.djinni.domain.repository.ApplicationRepository
import not.djinni.domain.usecase.core.UseCaseWithParams
import not.djinni.model.application.ApplicationDetails
import org.koin.core.annotation.Factory

@Factory
class GetApplicationDetailsUseCase(
    private val applicationRepository: ApplicationRepository,
) : UseCaseWithParams<Result<ApplicationDetails>, Long> {

    override suspend fun invoke(params: Long): Result<ApplicationDetails> {
        return applicationRepository.getApplicationDetails(params)
    }
}
