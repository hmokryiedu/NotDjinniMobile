package not.djinni.network.auth

import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.model.response.AuthResponse

interface AuthDataSource {

    suspend fun signIn(email: String, password: String): NetworkResponse<AuthResponse>
    suspend fun signUp(email: String, password: String): NetworkResponse<AuthResponse>

    suspend fun logOut(): Result<Unit>
}