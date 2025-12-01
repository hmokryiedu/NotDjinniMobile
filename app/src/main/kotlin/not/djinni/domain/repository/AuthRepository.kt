package not.djinni.domain.repository

interface AuthRepository {

    suspend fun getIsLoggedIn(): Boolean

    suspend fun signIn(email: String, password: String)
    suspend fun signUp(name: String, email: String, password: String)
    suspend fun refreshToken()

    suspend fun logOut(): Result<Unit>
}