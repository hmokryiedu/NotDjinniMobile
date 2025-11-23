package not.djinni.datastore.session

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import not.djinni.datastore.extension.getValue
import org.koin.core.annotation.Single

@Single(binds = [SessionDataStore::class])
internal class DefaultSessionDataStore(
    private val dataStore: DataStore<Preferences>
) : SessionDataStore {

    private val sessionKey = stringPreferencesKey(SESSION_TOKEN)

    override suspend fun setSessionToken(token: String) {
        dataStore.edit { it[sessionKey] = token }
    }

    override suspend fun getSessionToken(): String? {
        return dataStore.getValue(sessionKey)
    }

    override suspend fun clearToken() {
        dataStore.edit { it.remove(sessionKey) }
    }

    private companion object {
        const val SESSION_TOKEN = "session_token"
    }
}