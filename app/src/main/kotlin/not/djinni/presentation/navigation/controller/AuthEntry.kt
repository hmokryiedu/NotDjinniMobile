package not.djinni.presentation.navigation.controller

import androidx.navigation3.runtime.EntryProviderScope
import not.djinni.model.role.Role
import not.djinni.presentation.navigation.NavigationController
import not.djinni.presentation.screens.auth.AuthScreen
import not.djinni.presentation.screens.auth.role.ChooseRoleScreen

fun EntryProviderScope<Screens>.authEntry(
    controller: NavigationController,
) {
    entry<Screens.Auth> {
        AuthScreen(onNext = { controller.replaceAll(Screens.ChooseRole) })
    }
    entry<Screens.ChooseRole> {
        ChooseRoleScreen(
            onMain = { role -> controller.replaceAll(role.toMainScreen()) }
        )
    }
}

private fun Role.toMainScreen(): Screens {
    return when (this) {
        Role.SEEKER -> Screens.Seeker.Main
        Role.EMPLOYER -> Screens.Employer.Main
    }
}
