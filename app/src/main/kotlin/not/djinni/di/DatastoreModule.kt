package not.djinni.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import not.djinni.datastore.session.DefaultSessionDataStore
import not.djinni.datastore.session.SessionDataStore
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Module
class DatastoreModule {

    @Single
    @Named(SESSION_PREFERENCES_NAME)
    fun provideSessionPreferencesDataStore(context: Context): DataStore<Preferences> {
        return context.createDataStore(SESSION_PREFERENCES_NAME)
    }

    @Single
    fun provideSessionDataStore(
        @Named(SESSION_PREFERENCES_NAME) dataStore: DataStore<Preferences>
    ): SessionDataStore {
        return DefaultSessionDataStore(dataStore = dataStore)
    }

    private fun Context.createDataStore(name: String) = PreferenceDataStoreFactory.create(
        produceFile = { preferencesDataStoreFile(name) }
    )

    private companion object {
        const val SESSION_PREFERENCES_NAME = "session_preferences"
    }
}