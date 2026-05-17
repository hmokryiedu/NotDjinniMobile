@file:OptIn(KoinExperimentalAPI::class)

package not.djinni.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import not.djinni.presentation.navigation.controller.authEntry
import not.djinni.presentation.navigation.controller.employerEntry
import not.djinni.presentation.navigation.controller.publicEntry
import not.djinni.presentation.navigation.controller.seekerEntry
import not.djinni.presentation.navigation.controller.splashEntry
import org.koin.core.annotation.KoinExperimentalAPI

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
            authEntry(controller = controller)
            employerEntry(controller = controller)
            publicEntry(controller = controller)
            seekerEntry(controller = controller)
            splashEntry(controller = controller)
        },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        predictivePopTransitionSpec = {
            fadeIn(tween(ANIMATION_DURATION)) togetherWith
                    fadeOut(tween(ANIMATION_DURATION))
        },
        transitionSpec = {
            fadeIn(tween(ANIMATION_DURATION)) togetherWith
                    fadeOut(tween(ANIMATION_DURATION))
        },
        popTransitionSpec = {
            fadeIn(tween(ANIMATION_DURATION)) togetherWith
                    fadeOut(tween(ANIMATION_DURATION))
        },
    )
}

private const val ANIMATION_DURATION = 600
