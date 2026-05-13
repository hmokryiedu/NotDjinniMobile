package not.djinni.data.auth

import io.ktor.client.plugins.auth.providers.BearerTokens
import not.djinni.datastore.session.SessionDataStore
import not.djinni.network.auth.AuthDataSource
import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.token.request.RefreshTokenRequest
import org.koin.core.annotation.Single

@Single(binds = [AuthTokenManager::class])
internal class DefaultAuthTokenManager(
    private val authDataSource: AuthDataSource,
    private val sessionDataStore: SessionDataStore,
) : AuthTokenManager {

    override suspend fun loadTokens(): BearerTokens? {
        val token = sessionDataStore.getAccessSessionToken() ?: return null
        return BearerTokens(accessToken = token, refreshToken = null)
    }

    override suspend fun refreshTokens(): BearerTokens? {
        val request = sessionDataStore.getRefreshSessionToken()
            ?.let(::RefreshTokenRequest)
            ?: return clearAndReturnNull()

        return when (val response = authDataSource.refresh(request)) {
            is NetworkResponse.Success -> {
                sessionDataStore.setRefreshSessionToken(response.data.refreshToken)
                sessionDataStore.setAccessSessionToken(response.data.accessToken)
                BearerTokens(accessToken = response.data.accessToken, refreshToken = null)
            }

            is NetworkResponse.Error -> clearAndReturnNull()
        }
    }

    override suspend fun clearTokens() {
        sessionDataStore.clearTokens()
    }

    private suspend fun clearAndReturnNull(): BearerTokens? {
        clearTokens()
        return null
    }
}
