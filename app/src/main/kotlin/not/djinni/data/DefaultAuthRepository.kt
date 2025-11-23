package not.djinni.data

import not.djinni.data.mapper.toDomain
import not.djinni.domain.repository.AuthRepository
import not.djinni.model.User
import not.djinni.network.auth.AuthDataSource
import not.djinni.network.common.response.NetworkResponse
import org.koin.core.annotation.Single

@Single(binds = [AuthRepository::class])
internal class DefaultAuthRepository(
    private val authDataSource: AuthDataSource,
) : AuthRepository {

    override suspend fun getIsLoggedIn(): Boolean {
        TODO()
    }

    override suspend fun signIn(email: String, password: String): User {
        val response = authDataSource.signIn(email = email, password = password)
        return when (response) {
            is NetworkResponse.Error -> throw Exception(response.error)
            is NetworkResponse.Success -> response.data.user.toDomain()
        }
    }

    override suspend fun signUp(email: String, password: String): User {
        val response = authDataSource.signUp(email = email, password = password)
        return when (response) {
            is NetworkResponse.Error -> throw Exception(response.error)
            is NetworkResponse.Success -> response.data.user.toDomain()
        }
    }

    override suspend fun logOut(): Result<Unit> {
        return authDataSource.logOut()
    }
}