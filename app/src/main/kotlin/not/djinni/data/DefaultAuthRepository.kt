package not.djinni.data

import not.djinni.data.mapper.toDomain
import not.djinni.datastore.session.SessionDataStore
import not.djinni.domain.repository.AuthRepository
import not.djinni.model.User
import not.djinni.network.auth.AuthDataSource
import not.djinni.network.common.response.NetworkResponse
import org.koin.core.annotation.Single

@Single(binds = [AuthRepository::class])
internal class DefaultAuthRepository(
    private val authDataSource: AuthDataSource,
    private val sessionDataStore: SessionDataStore,
) : AuthRepository {

    override suspend fun getIsLoggedIn(): Boolean {
        return sessionDataStore.getSessionToken() != null
    }

    override suspend fun signIn(email: String, password: String): User {
        val response = authDataSource.signIn(email = email, password = password)
        return when (response) {
            is NetworkResponse.Error -> throw Exception(response.error)
            is NetworkResponse.Success -> {
                sessionDataStore.setSessionToken(response.data.token)
                response.data.user.toDomain()
            }
        }
    }

    override suspend fun signUp(email: String, password: String): User {
        val response = authDataSource.signUp(email = email, password = password)
        return when (response) {
            is NetworkResponse.Error -> throw Exception(response.error)
            is NetworkResponse.Success -> {
                sessionDataStore.setSessionToken(response.data.token)
                response.data.user.toDomain()
            }
        }
    }

    override suspend fun logOut(): Result<Unit> {
        sessionDataStore.clearToken()
        return authDataSource.logOut()
    }
}