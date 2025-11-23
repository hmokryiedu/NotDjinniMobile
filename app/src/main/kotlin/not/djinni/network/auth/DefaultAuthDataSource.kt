package not.djinni.network.auth

import io.ktor.client.HttpClient
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.setBody
import not.djinni.network.auth.resource.Auth
import not.djinni.network.common.extension.networkResponse
import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.model.request.LoginRequest
import not.djinni.network.model.response.AuthResponse
import org.koin.core.annotation.Single

@Single(binds = [AuthDataSource::class])
internal class DefaultAuthDataSource(
    private val httpClient: HttpClient
) : AuthDataSource {

    override suspend fun signIn(email: String, password: String): NetworkResponse<AuthResponse> {
        val request = LoginRequest(email = email, password = password)
        return httpClient
            .post(Auth.Login()) { setBody(request) }
            .networkResponse<AuthResponse>()
    }

    override suspend fun signUp(email: String, password: String): NetworkResponse<AuthResponse> {
        val request = LoginRequest(email = email, password = password)
        return httpClient
            .post(Auth.Register()) { setBody(request) }
            .networkResponse<AuthResponse>()
    }

    override suspend fun logOut(): Result<Unit> = runCatching {
        TODO("Not yet implemented")
    }
}