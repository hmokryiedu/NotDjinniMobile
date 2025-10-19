package not.djinni.presentation.navigation.extension

import androidx.navigation.NavController
import not.djinni.presentation.navigation.controller.Screens

internal fun NavController.navigateSingleTop(screen: Screens) {
    navigate(screen) {
        launchSingleTop = true
        popUpTo(0) { inclusive = true }
    }
}