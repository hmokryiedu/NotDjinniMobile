package not.djinni.network.auth

import io.ktor.client.HttpClient
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.setBody
import not.djinni.network.auth.resource.Auth
import not.djinni.network.auth.response.AuthResponse
import not.djinni.network.common.extension.networkResponse
import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.model.request.auth.SignInRequest
import not.djinni.network.model.request.auth.SignUpRequest
import not.djinni.network.token.request.RefreshTokenRequest
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single(binds = [AuthDataSource::class])
internal class DefaultAuthDataSource(
    @Named("public") private val httpClient: HttpClient
) : AuthDataSource {

    override suspend fun signIn(email: String, password: String): NetworkResponse<AuthResponse> {
        val request = SignInRequest(email = email, password = password)
        return httpClient
            .post(Auth.Login()) { setBody(request) }
            .networkResponse<AuthResponse>()
    }

    override suspend fun signUp(
        name: String,
        email: String,
        password: String
    ): NetworkResponse<AuthResponse> {
        val request = SignUpRequest(name = name, email = email, password = password)
        return httpClient
            .post(Auth.Register()) { setBody(request) }
            .networkResponse<AuthResponse>()
    }

    override suspend fun refresh(request: RefreshTokenRequest): NetworkResponse<AuthResponse> {
        return httpClient
            .post(Auth.Refresh()) { setBody(request) }
            .networkResponse<AuthResponse>()
    }

    override suspend fun logOut(): Result<Unit> = runCatching {
        TODO("Not yet implemented")
    }
}