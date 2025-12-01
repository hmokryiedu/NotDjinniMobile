package not.djinni.presentation.core.components.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.currentStateAsState

@Composable
inline fun <reified T : Any> AlertContainer(
    alert: T?,
    content: @Composable (T) -> Unit
) {
    val isResumedLifecycle by rememberIsLifecycleAtLeast(Lifecycle.State.RESUMED)
    if (isResumedLifecycle && alert != null) content(alert)
}

@Composable
fun rememberIsLifecycleAtLeast(state: Lifecycle.State): State<Boolean> {
    val lifecycleOwner by LocalLifecycleOwner.current.lifecycle.currentStateAsState()
    return remember(lifecycleOwner) {
        derivedStateOf { lifecycleOwner.isAtLeast(state) }
    }
}