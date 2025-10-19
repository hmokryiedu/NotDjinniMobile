package not.djinni.presentation.navigation.controller

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import not.djinni.presentation.screens.auth.AuthScreen

@Serializable
data object Auth : Screens

fun NavGraphBuilder.authRoute() {
    composable<Auth> { AuthScreen() }
}
