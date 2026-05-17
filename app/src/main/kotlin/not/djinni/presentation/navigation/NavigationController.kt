package not.djinni.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.serialization.json.Json
import not.djinni.presentation.navigation.controller.Screens

@Stable
class NavigationController private constructor(initialKey: Screens) {

    private val _stack = mutableStateListOf(initialKey)
    private val navResultStore = NavResultStore()
    val stack: SnapshotStateList<Screens> = _stack

    fun navigate(key: Screens) {
        _stack.add(key)
    }

    fun popBackStack() {
        _stack.removeLastOrNull()
    }

    fun replaceAll(key: Screens) {
        _stack.clear()
        _stack.add(key)
    }

    fun popUpTo(key: Screens, to: Screens, inclusive: Boolean = false) {
        val index = _stack.lastIndexOf(to)
        if (index != -1) {
            val removeFrom = if (inclusive) index else index + 1
            _stack.removeRange(removeFrom, _stack.size)
        }
        _stack.add(key)
    }

    fun <T> navigateForResult(key: Screens, resultKey: NavResultKey<T>) {
        navigate(key)
    }

    fun <T> popWithResult(resultKey: NavResultKey<T>, value: T) {
        navResultStore.put(resultKey.id, resultKey.contract.encode(value))
        popBackStack()
    }

    fun <T> consumeResult(resultKey: NavResultKey<T>): T? {
        val encoded = navResultStore.consume(resultKey.id) ?: return null
        return resultKey.contract.decode(encoded)
    }

    companion object {
        @Composable
        fun rememberNavigationController(initialKey: Screens): NavigationController {
            return rememberSaveable(
                saver = listSaver(
                    save = {
                        listOf(
                            it.stack.map(Json::encodeToString),
                            it.navResultStore.snapshot().toList()
                        )
                    },
                    restore = {
                        @Suppress("UNCHECKED_CAST")
                        val stack = it[0] as List<String>
                        @Suppress("UNCHECKED_CAST")
                        val pendingResults = it[1] as List<Pair<String, String>>
                        NavigationController(initialKey).apply {
                            _stack.clear()
                            _stack.addAll(stack.map(Json::decodeFromString))
                            pendingResults.forEach { (key, value) -> navResultStore.put(key, value) }
                        }
                    }
                ),
                init = { NavigationController(initialKey) }
            )
        }
    }
}
