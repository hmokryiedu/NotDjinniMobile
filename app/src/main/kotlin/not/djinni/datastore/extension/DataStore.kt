package not.djinni.datastore.extension

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.firstOrNull

suspend inline fun <reified T : Any> DataStore<Preferences>.getValue(key: Preferences.Key<T>): T? {
    return data.firstOrNull()?.get(key)
}