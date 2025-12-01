package not.djinni.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import not.djinni.presentation.navigation.controller.authEntry
import not.djinni.presentation.navigation.controller.employerEntry
import not.djinni.presentation.navigation.controller.seekerEntry
import not.djinni.presentation.navigation.controller.splashEntry

@Composable
fun NotDjinniNavDisplay(
    modifier: Modifier = Modifier,
    controller: NavigationController,
) {
    NavDisplay(
        modifier = modifier,
        backStack = controller.stack,
        onBack = controller::popBackStack,
        entryProvider = entryProvider {
            splashEntry(controller = controller)
            authEntry(controller = controller)
            seekerEntry(controller = controller)
            employerEntry(controller = controller)
        },
        transitionSpec = {
            fadeIn(tween(ANIMATION_DURATION)) togetherWith
                    fadeOut(tween(ANIMATION_DURATION))
        },
        popTransitionSpec = {
            fadeIn(tween(ANIMATION_DURATION)) togetherWith
                    fadeOut(tween(ANIMATION_DURATION))
        }
    )
}

private const val ANIMATION_DURATION = 500