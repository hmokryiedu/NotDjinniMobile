package not.djinni.domain.usecase.application

import not.djinni.domain.repository.ApplicationRepository
import not.djinni.domain.usecase.core.UseCaseWithParams
import not.djinni.model.application.ApplicationStatus
import org.koin.core.annotation.Factory

@Factory
class UpdateApplicationStatusUseCase(
    private val applicationRepository: ApplicationRepository,
) : UseCaseWithParams<Result<Unit>, UpdateApplicationStatusUseCase.Params> {

    override suspend fun invoke(params: Params): Result<Unit> {
        return applicationRepository.updateApplicationStatus(
            id = params.applicationId,
            status = params.status
        )
    }

    data class Params(
        val applicationId: Long,
        val status: ApplicationStatus,
    )
}
