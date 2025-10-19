package not.djinni.presentation.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import not.djinni.core.logging.error
import not.djinni.presentation.core.components.base.model.SnackBarData
import java.util.UUID
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.cancellation.CancellationException

abstract class BaseViewModel : ViewModel() {

    private val _loadingSet = MutableStateFlow<Set<String>>(emptySet())
    val loading = _loadingSet.map { it.isNotEmpty() }

    private val _snackBarData = Channel<SnackBarData>()
    val snackBarData = _snackBarData.receiveAsFlow()

    private val actionsMap = mutableMapOf<String, () -> Unit>()

    fun launch(
        context: CoroutineContext = EmptyCoroutineContext,
        actionId: String = UUID.randomUUID().toString(),
        loadingEnabled: Boolean = false,
        block: suspend CoroutineScope.() -> Unit,
    ): Job {
        saveAction(context, actionId, block)
        return viewModelScope.launch(context = context) {
            try {
                if (loadingEnabled) addLoading(actionId)
                block()
            } catch (throwable: Throwable) {
                if(throwable is CancellationException) return@launch
                error("BaseViewModel", throwable) { "Error in action: $actionId" }
            } finally {
                removeLoading(actionId)
            }
        }
    }

    fun showSnackBar(data: SnackBarData) {
        _snackBarData.trySend(data)
    }

    private fun addLoading(actionId: String) {
        _loadingSet.update { it + actionId }
    }

    private fun removeLoading(actionId: String) {
        _loadingSet.update { it - actionId }
    }

    private fun saveAction(
        context: CoroutineContext,
        actionId: String,
        action: suspend CoroutineScope.() -> Unit,
    ) {
        actionsMap[actionId] = {
            launch(
                context = context,
                actionId = actionId,
                block = action,
            )
        }
    }
}

abstract class StateViewModel<S>(
    initialState: S,
) : BaseViewModel() {
    protected val mutableState = MutableStateFlow(initialState)
    val state = mutableState.asStateFlow()
}