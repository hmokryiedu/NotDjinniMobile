package not.djinni.data.repository

import not.djinni.data.mapper.toDomain
import not.djinni.domain.repository.UserRepository
import not.djinni.model.User
import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.user.UserDataSource
import org.koin.core.annotation.Single

@Single(binds = [UserRepository::class])
class DefaultUserRepository(
    private val remoteDataSource: UserDataSource,
) : UserRepository {

    override suspend fun getUser(): User? {
        return when (val response = remoteDataSource.getUser()) {
            is NetworkResponse.Success -> response.data.toDomain()
            is NetworkResponse.Error -> null
        }
    }
}
