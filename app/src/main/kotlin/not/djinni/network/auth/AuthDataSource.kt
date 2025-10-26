package not.djinni.network.auth

import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.model.response.TokenResponse

interface AuthDataSource {

    suspend fun signIn(email: String, password: String): NetworkResponse<TokenResponse>
    suspend fun signUp(email: String, password: String): NetworkResponse<TokenResponse>

    suspend fun logOut(): Result<Unit>
}