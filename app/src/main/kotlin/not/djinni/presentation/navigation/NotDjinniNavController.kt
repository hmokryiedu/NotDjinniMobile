package not.djinni.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import not.djinni.presentation.navigation.controller.Screens
import not.djinni.presentation.navigation.controller.splashRoute

@Composable
fun NotDjinniNavController(
    modifier: Modifier = Modifier,
    startDestination: Screens,
    controller: NavHostController,
) {
    NavHost(
        modifier = modifier,
        navController = controller,
        startDestination = startDestination,
        enterTransition = { fadeIn(tween(ANIMATION_DURATION)) },
        exitTransition = { fadeOut(tween(ANIMATION_DURATION)) }
    ) {
        splashRoute()
    }
}

private const val ANIMATION_DURATION = 500