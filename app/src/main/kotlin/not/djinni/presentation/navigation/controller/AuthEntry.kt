package not.djinni.presentation.navigation.controller

import androidx.navigation3.runtime.EntryProviderScope
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
            onSeekerMain = { controller.replaceAll(Screens.Seeker.Main) },
            onEmployerMain = { controller.replaceAll(Screens.Employer.Main) },
            onSeekerCreateProfile = { controller.navigate(Screens.Seeker.CreateProfile) },
            onEmployerCreateProfile = { controller.navigate(Screens.Employer.CreateProfile) },
        )
    }
}
