package not.djinni.network.auth

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import not.djinni.network.auth.model.LoginRequest
import not.djinni.network.auth.model.TokenResponse
import not.djinni.network.auth.resource.Auth
import not.djinni.network.common.response.ErrorResponse
import not.djinni.network.common.response.NetworkResponse
import org.koin.core.annotation.Single

@Single(binds = [AuthDataSource::class])
internal class DefaultAuthDataSource(
    private val httpClient: HttpClient
) : AuthDataSource {

    override suspend fun signIn(email: String, password: String): NetworkResponse<TokenResponse> {
        val request = LoginRequest(email = email, password = password)
        return httpClient
            .post(Auth.Login()) { setBody(request) }
            .networkResponse<TokenResponse>()
    }

    override suspend fun signUp(email: String, password: String): NetworkResponse<TokenResponse> {
        val request = LoginRequest(email = email, password = password)
        return httpClient
            .post(Auth.Register()) { setBody(request) }
            .networkResponse<TokenResponse>()
    }

    override suspend fun logOut(): Result<Unit> = runCatching {
        TODO("Not yet implemented")
    }

    private suspend inline fun <reified T : Any> HttpResponse.networkResponse(): NetworkResponse<T> {
        return when (status.value) {
            in 200..299 -> NetworkResponse.Success(body<T>())
            else -> NetworkResponse.Error(body<ErrorResponse>().message)
        }
    }
}