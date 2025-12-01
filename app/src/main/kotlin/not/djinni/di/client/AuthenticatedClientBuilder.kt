package not.djinni.di.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.resources.Resources
import not.djinni.datastore.session.SessionDataStore
import not.djinni.domain.repository.AuthRepository
import org.koin.core.annotation.Single

@Single
class AuthenticatedClientBuilder(
    private val authRepository: AuthRepository,
    private val sessionDataStore: SessionDataStore,
) : BaseClientBuilder() {

    override fun create(): HttpClient {
        return HttpClient(OkHttp) {
            setupEngine()
            installContentNegotiation()
            install(Resources)
            installDefaultRequest()
            installAuth()
        }
    }

    private fun HttpClientConfig<*>.installAuth() {
        install(Auth) {
            bearer {
                loadTokens {
                    val token = sessionDataStore.getAccessSessionToken() ?: return@loadTokens null
                    BearerTokens(accessToken = token, refreshToken = null)
                }
                refreshTokens {
                    authRepository.refreshToken()
                    val newToken =
                        sessionDataStore.getAccessSessionToken() ?: return@refreshTokens null
                    BearerTokens(accessToken = newToken, refreshToken = null)
                }
            }
        }
    }
}