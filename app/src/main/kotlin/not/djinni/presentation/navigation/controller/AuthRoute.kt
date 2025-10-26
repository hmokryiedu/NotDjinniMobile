package not.djinni.presentation.navigation.controller

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import not.djinni.presentation.navigation.extension.navigateSingleTop
import not.djinni.presentation.screens.auth.AuthScreen

@Serializable
data object Auth : Screens

fun NavGraphBuilder.authRoute(controller: NavController) {
    composable<Auth> {
        AuthScreen(
            onNavigateToMain = { controller.navigateSingleTop(Main) }
        )
    }
}
