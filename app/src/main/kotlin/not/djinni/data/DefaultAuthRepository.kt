package not.djinni.data

import not.djinni.domain.repository.AuthRepository
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

    override suspend fun signIn(email: String, password: String) {
        val response = authDataSource.signIn(email = email, password = password)
        when (response) {
            is NetworkResponse.Error -> throw Exception(response.error)
            is NetworkResponse.Success<*> -> Unit
        }
    }

    override suspend fun signUp(email: String, password: String) {
        val response = authDataSource.signUp(email = email, password = password)
        when (response) {
            is NetworkResponse.Error -> throw Exception(response.error)
            is NetworkResponse.Success<*> -> Unit
        }
    }

    override suspend fun logOut(): Result<Unit> {
        return authDataSource.logOut()
    }
}