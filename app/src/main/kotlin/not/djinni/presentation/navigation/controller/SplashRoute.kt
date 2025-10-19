package not.djinni.presentation.navigation.controller

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import not.djinni.presentation.navigation.extension.navigateSingleTop
import not.djinni.presentation.screens.splash.SplashScreen

@Serializable
data object Splash : Screens

fun NavGraphBuilder.splashRoute(controller: NavController) {
    composable<Splash> {
        SplashScreen(
            onAuth = { controller.navigateSingleTop(Auth) }
        )
    }
}