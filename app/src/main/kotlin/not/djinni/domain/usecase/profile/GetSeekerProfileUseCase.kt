package not.djinni.domain.usecase.profile

import not.djinni.domain.repository.ProfileRepository
import not.djinni.domain.usecase.core.UseCase
import not.djinni.model.SeekerProfile
import org.koin.core.annotation.Factory

@Factory
class GetSeekerProfileUseCase(
    private val profileRepository: ProfileRepository
) : UseCase<SeekerProfile?> {

    override suspend fun invoke(): SeekerProfile? {
        return profileRepository.getSeekerProfile()
    }
}
