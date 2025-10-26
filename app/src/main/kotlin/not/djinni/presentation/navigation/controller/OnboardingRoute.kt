package not.djinni.presentation.navigation.controller

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import not.djinni.presentation.navigation.extension.navigateSingleTop
import not.djinni.presentation.screens.onboarding.OnboardingScreen

@Serializable
data object Onboarding : Screens

fun NavGraphBuilder.onboardingRoute(controller: NavHostController) {
    composable<Onboarding> {
        OnboardingScreen(
            onMain = { controller.navigateSingleTop(Main) }
        )
    }
}