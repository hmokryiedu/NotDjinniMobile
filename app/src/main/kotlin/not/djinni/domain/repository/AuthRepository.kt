package not.djinni.domain.repository

import not.djinni.model.User

interface AuthRepository {

    suspend fun getIsLoggedIn(): Boolean

    suspend fun signIn(email: String, password: String): User
    suspend fun signUp(email: String, password: String): User

    suspend fun logOut(): Result<Unit>
}