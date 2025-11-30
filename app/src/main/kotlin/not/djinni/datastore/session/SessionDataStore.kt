package not.djinni.datastore.session

interface SessionDataStore {

    suspend fun setAccessSessionToken(token: String)
    suspend fun getAccessSessionToken(): String?

    suspend fun setRefreshSessionToken(token: String)
    suspend fun getRefreshSessionToken(): String?

    suspend fun clearTokens()
}