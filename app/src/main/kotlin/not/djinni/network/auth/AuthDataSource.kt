package not.djinni.network.auth

import not.djinni.network.auth.response.AuthResponse
import not.djinni.network.common.response.NetworkResponse

interface AuthDataSource {

    suspend fun signIn(email: String, password: String): NetworkResponse<AuthResponse>
    suspend fun signUp(
        name: String,
        email: String,
        password: String
    ): NetworkResponse<AuthResponse>

    suspend fun logOut(): Result<Unit>
}