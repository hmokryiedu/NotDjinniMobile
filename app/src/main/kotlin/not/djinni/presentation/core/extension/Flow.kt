package not.djinni.presentation.core.extension

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow

@SuppressLint("ComposableNaming")
@Composable
fun <T> Flow<T>.collectAsEffect(action: suspend (T) -> Unit) {
    LaunchedEffect(Unit) {
        collect(action)
    }
}

@SuppressLint("ComposableNaming")
@Composable
fun Flow<*>.collectEventAsEffect(action: suspend () -> Unit) {
    LaunchedEffect(Unit) {
        collect {
            action()
        }
    }
}