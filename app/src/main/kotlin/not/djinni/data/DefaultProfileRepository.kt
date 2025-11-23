package not.djinni.data

import not.djinni.data.mapper.toDomain
import not.djinni.domain.repository.ProfileRepository
import not.djinni.model.EmployerProfile
import not.djinni.model.SeekerProfile
import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.profile.ProfileDataSource
import org.koin.core.annotation.Single

@Single(binds = [ProfileRepository::class])
internal class DefaultProfileRepository(
    private val profileDataSource: ProfileDataSource
) : ProfileRepository {

    override suspend fun getSeekerProfile(): SeekerProfile? {
        return when (val response = profileDataSource.getSeekerProfile()) {
            is NetworkResponse.Success -> response.data.toDomain()
            is NetworkResponse.Error -> null
        }
    }

    override suspend fun getEmployerProfile(): EmployerProfile? {
        return when (val response = profileDataSource.getEmployerProfile()) {
            is NetworkResponse.Success -> response.data.toDomain()
            is NetworkResponse.Error -> null
        }
    }
}
