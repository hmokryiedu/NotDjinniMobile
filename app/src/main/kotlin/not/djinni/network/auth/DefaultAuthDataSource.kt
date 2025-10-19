package not.djinni.network.auth

import org.koin.core.annotation.Single

@Single(binds = [AuthDataSource::class])
internal class DefaultAuthDataSource : AuthDataSource {

    override suspend fun signIn(email: String, password: String) {
        TODO("Not yet implemented")
    }

    override suspend fun signUp(email: String, password: String) {
        TODO("Not yet implemented")
    }

    override suspend fun logOut(): Result<Unit> = runCatching {
        TODO("Not yet implemented")
    }
}