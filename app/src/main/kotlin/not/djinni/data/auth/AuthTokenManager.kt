package not.djinni.data.auth

import io.ktor.client.plugins.auth.providers.BearerTokens

interface AuthTokenManager {

    suspend fun loadTokens(): BearerTokens?
    suspend fun refreshTokens(): BearerTokens?
    suspend fun clearTokens()
}
