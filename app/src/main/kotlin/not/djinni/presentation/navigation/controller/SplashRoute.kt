package not.djinni.presentation.navigation.controller

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import not.djinni.presentation.screens.splash.SplashScreen

@Serializable
data object Splash : Screens

fun NavGraphBuilder.splashRoute() {
    composable<Splash> { SplashScreen() }
}