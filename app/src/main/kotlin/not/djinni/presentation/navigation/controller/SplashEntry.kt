package not.djinni.presentation.navigation.controller

import androidx.navigation3.runtime.EntryProviderScope
import not.djinni.presentation.navigation.NavigationController
import not.djinni.presentation.screens.splash.SplashScreen

fun EntryProviderScope<Screens>.splashEntry(
    controller: NavigationController,
) {
    entry<Screens.Splash> {
        SplashScreen(
            onAuth = { controller.replaceAll(Screens.Auth) },
            onChooseRole = { controller.replaceAll(Screens.ChooseRole) }
        )
    }
}