package not.djinni.datastore.session

interface SessionDataStore {

    suspend fun setSessionToken(token: String)
    suspend fun getSessionToken(): String?
    suspend fun clearToken()
}