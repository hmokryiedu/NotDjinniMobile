package not.djinni.data

import not.djinni.domain.repository.AuthRepository
import not.djinni.network.auth.AuthDataSource
import org.koin.core.annotation.Single

@Single(binds = [AuthRepository::class])
internal class DefaultAuthRepository(
    private val authDataSource: AuthDataSource
) : AuthRepository {

    override suspend fun getIsLoggedIn(): Boolean {
        TODO()
    }

    override suspend fun signIn(email: String, password: String) {
        authDataSource.signIn(email = email, password = password)
    }

    override suspend fun signUp(email: String, password: String) {
        authDataSource.signUp(email = email, password = password)
    }

    override suspend fun logOut(): Result<Unit> {
        return authDataSource.logOut()
    }
}