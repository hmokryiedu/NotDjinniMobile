package not.djinni.di.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.resources.Resources
import not.djinni.data.auth.AuthTokenManager
import org.koin.core.annotation.Single

@Single
class AuthenticatedClientBuilder(
    private val authTokenManager: AuthTokenManager,
) : BaseClientBuilder() {

    override fun create(): HttpClient {
        return HttpClient(OkHttp) {
            setupEngine()
            installContentNegotiation()
            install(Resources)
            installDefaultRequest()
            installLogging()
            installAuth()
        }
    }

    private fun HttpClientConfig<*>.installAuth() {
        install(Auth) {
            bearer {
                loadTokens {
                    authTokenManager.loadTokens()
                }
                refreshTokens {
                    authTokenManager.refreshTokens()
                }
            }
        }
    }
}
