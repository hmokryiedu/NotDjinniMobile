package not.djinni.domain.usecase.application

import not.djinni.domain.repository.ApplicationRepository
import not.djinni.domain.usecase.core.UseCase
import not.djinni.model.application.ApplicationDetails
import org.koin.core.annotation.Factory

@Factory
class GetSeekerApplicationsUseCase(
    private val applicationRepository: ApplicationRepository,
) : UseCase<Result<List<ApplicationDetails>>> {

    override suspend fun invoke(): Result<List<ApplicationDetails>> {
        return applicationRepository.getSeekerApplications()
    }
}
