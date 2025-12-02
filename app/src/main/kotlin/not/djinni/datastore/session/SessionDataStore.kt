package not.djinni.datastore.session

import not.djinni.model.role.Role

interface SessionDataStore {

    suspend fun setAccessSessionToken(token: String)
    suspend fun getAccessSessionToken(): String?

    suspend fun setRefreshSessionToken(token: String)
    suspend fun getRefreshSessionToken(): String?

    suspend fun setCurrentRole(role: Role)
    suspend fun getCurrentRole(): Role?

    suspend fun clearTokens()
}