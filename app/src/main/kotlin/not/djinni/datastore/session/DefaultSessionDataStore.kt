package not.djinni.datastore.session

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import not.djinni.datastore.extension.getValue

internal class DefaultSessionDataStore(
    private val dataStore: DataStore<Preferences>
) : SessionDataStore {

    private val accessSessionToken = stringPreferencesKey(SESSION_TOKEN)
    private val refreshSessionToken = stringPreferencesKey(REFRESH_SESSION_TOKEN)

    override suspend fun setRefreshSessionToken(token: String) {
        dataStore.edit { it[refreshSessionToken] = token }
    }

    override suspend fun getRefreshSessionToken(): String? {
        return dataStore.getValue(refreshSessionToken)
    }

    override suspend fun setAccessSessionToken(token: String) {
        dataStore.edit { it[accessSessionToken] = token }
    }

    override suspend fun getAccessSessionToken(): String? {
        return dataStore.getValue(accessSessionToken)
    }

    override suspend fun clearTokens() {
        dataStore.edit {
            it.remove(accessSessionToken)
            it.remove(refreshSessionToken)
        }
    }

    private companion object {
        const val SESSION_TOKEN = "session_token"
        const val REFRESH_SESSION_TOKEN = "refresh_session_token"
    }
}