package not.djinni.presentation.navigation.controller

import androidx.navigation3.runtime.EntryProviderScope
import not.djinni.presentation.navigation.NavigationController
import not.djinni.presentation.screens.seeker.main.MainSeekerScreen
import not.djinni.presentation.screens.seeker.profile.create.CreateSeekerProfileScreen
import not.djinni.presentation.screens.seeker.profile.view.SeekerProfileScreen
import not.djinni.presentation.screens.seeker.vacancy.all.AllVacanciesScreen
import not.djinni.presentation.screens.seeker.vacancy.applied.AppliedVacanciesScreen
import not.djinni.presentation.screens.seeker.vacancy.details.VacancyDetailsScreen

fun EntryProviderScope<Screens>.seekerEntry(
    controller: NavigationController,
) {
    entry<Screens.Seeker.CreateProfile> {
        CreateSeekerProfileScreen(
            onHome = { controller.replaceAll(Screens.Seeker.Main) }
        )
    }
    entry<Screens.Seeker.Main> {
        MainSeekerScreen(
            onVacancyClick = { vacancyId ->
                controller.navigate(Screens.Seeker.VacancyDetails(vacancyId = vacancyId))
            },
            onProfileClick = { controller.navigate(Screens.Seeker.Profile) }
        )
    }
    entry<Screens.Seeker.AllVacancies> {
        AllVacanciesScreen()
    }
    entry<Screens.Seeker.AppliedVacancies> {
        AppliedVacanciesScreen()
    }
    entry<Screens.Seeker.VacancyDetails> { entry ->
        VacancyDetailsScreen(
            vacancyId = entry.vacancyId,
            onNavigateBack = controller::popBackStack
        )
    }
    entry<Screens.Seeker.Profile> {
        SeekerProfileScreen(
            onChangeRole = { controller.replaceAll(Screens.ChooseRole) }
        )
    }
}