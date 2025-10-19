package not.djinni.network.auth

interface AuthDataSource {

    suspend fun signIn(email: String, password: String)
    suspend fun signUp(email: String, password: String)

    suspend fun logOut(): Result<Unit>
}