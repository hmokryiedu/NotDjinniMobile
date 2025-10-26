package not.djinni.presentation.navigation.controller

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import not.djinni.presentation.screens.main.MainScreen

@Serializable
data object Main : Screens

fun NavGraphBuilder.mainRoute() {
    composable<Main> { MainScreen() }
}
