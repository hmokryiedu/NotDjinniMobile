package not.djinni.data.repository

import not.djinni.data.mapper.toDomain
import not.djinni.domain.repository.SeekerRepository
import not.djinni.model.seeker.SeekerProfile
import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.seeker.SeekerDataSource
import org.koin.core.annotation.Single

@Single(binds = [SeekerRepository::class])
class DefaultSeekerRepository(
    private val remoteDataSource: SeekerDataSource,
) : SeekerRepository {

    override suspend fun getProfile(): SeekerProfile? {
        return when (val response = remoteDataSource.getProfile()) {
            is NetworkResponse.Success -> response.data.toDomain()
            is NetworkResponse.Error -> null
        }
    }
}