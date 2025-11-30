package not.djinni.data.repository

import not.djinni.datastore.session.SessionDataStore
import not.djinni.domain.repository.AuthRepository
import not.djinni.network.auth.AuthDataSource
import not.djinni.network.common.response.NetworkResponse
import org.koin.core.annotation.Single

@Single(binds = [AuthRepository::class])
internal class DefaultAuthRepository(
    private val authDataSource: AuthDataSource,
    private val sessionDataStore: SessionDataStore,
) : AuthRepository {

    override suspend fun getIsLoggedIn(): Boolean {
        return sessionDataStore.getAccessSessionToken() != null
    }

    override suspend fun signIn(email: String, password: String) {
        when (val response = authDataSource.signIn(email = email, password = password)) {
            is NetworkResponse.Error -> throw Exception(response.error)
            is NetworkResponse.Success -> {
                sessionDataStore.setAccessSessionToken(response.data.accessToken)
                sessionDataStore.setRefreshSessionToken(response.data.refreshToken)
            }
        }
    }

    override suspend fun signUp(name: String, email: String, password: String) {
        val response = authDataSource.signUp(name = name, email = email, password = password)
        when (response) {
            is NetworkResponse.Error -> throw Exception(response.error)
            is NetworkResponse.Success -> {
                sessionDataStore.setAccessSessionToken(response.data.accessToken)
                sessionDataStore.setRefreshSessionToken(response.data.refreshToken)
            }
        }
    }

    override suspend fun logOut(): Result<Unit> {
        sessionDataStore.clearTokens()
        return authDataSource.logOut()
    }
}