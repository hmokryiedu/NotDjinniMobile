package not.djinni.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.serialization.json.Json
import not.djinni.presentation.navigation.controller.Screens

@Stable
class NavigationController private constructor(initialKey: Screens) {

    private val _stack = mutableStateListOf(initialKey)
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

    fun popUpTo(key: Screens, inclusive: Boolean = false) {
        val index = _stack.lastIndexOf(key)
        if (index != -1) {
            val removeFrom = if (inclusive) index else index + 1
            _stack.removeRange(removeFrom, _stack.size)
        }
    }

    companion object {
        @Composable
        fun rememberNavigationController(initialKey: Screens): NavigationController {
            return rememberSaveable(
                saver = Saver(
                    save = { it.stack.map(Json::encodeToString) },
                    restore = {
                        NavigationController(initialKey).apply {
                            _stack.clear()
                            _stack.addAll(it.map(Json::decodeFromString))
                        }
                    }
                ),
                init = { NavigationController(initialKey) }
            )
        }
    }
}