package not.djinni.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.rememberNavBackStack
import not.djinni.presentation.navigation.controller.Screens

@Stable
class NavigationController(
    val stack: NavBackStack<Screens>,
    private val onRootBack: () -> Unit,
) {
    fun navigate(route: Screens) {
        stack.add(route)
    }

    fun popBackStack(): Boolean {
        if (stack.size > 1) {
            stack.removeLastOrNull()
            return true
        }
        onRootBack()
        return false
    }

    fun replaceAll(route: Screens) {
        stack.clear()
        stack.add(route)
    }

    fun popUpTo(key: Screens, to: Screens, inclusive: Boolean = false): Boolean {
        val index = stack.lastIndexOf(to)
        if (index == -1) return false

        val removeFrom = if (inclusive) index else index + 1
        if (removeFrom < stack.size) {
            for (stackIndex in stack.lastIndex downTo removeFrom) {
                stack.removeAt(stackIndex)
            }
        }
        if (stack.isEmpty()) stack.add(to)
        stack.add(key)
        return true
    }

    companion object {
        @Composable
        fun rememberNavigationController(
            startRoute: Screens = Screens.Splash,
            onRootBack: () -> Unit = {},
        ): NavigationController {
            val stack = rememberScreensNavBackStack(startRoute)
            return remember(onRootBack, stack) {
                NavigationController(
                    stack = stack,
                    onRootBack = onRootBack,
                )
            }
        }

        @Composable
        @Suppress("UNCHECKED_CAST")
        private fun rememberScreensNavBackStack(route: Screens): NavBackStack<Screens> {
            return rememberNavBackStack(route) as NavBackStack<Screens>
        }
    }
}
