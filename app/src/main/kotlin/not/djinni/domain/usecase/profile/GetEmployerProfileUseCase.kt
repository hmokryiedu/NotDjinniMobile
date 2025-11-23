package not.djinni.domain.usecase.profile

import not.djinni.domain.repository.ProfileRepository
import not.djinni.domain.usecase.core.UseCase
import not.djinni.model.EmployerProfile
import org.koin.core.annotation.Factory

@Factory
class GetEmployerProfileUseCase(
    private val profileRepository: ProfileRepository
) : UseCase<EmployerProfile?> {

    override suspend fun invoke(): EmployerProfile? {
        return profileRepository.getEmployerProfile()
    }
}
