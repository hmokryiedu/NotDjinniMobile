package not.djinni.presentation.navigation.controller

import androidx.navigation3.runtime.EntryProviderScope
import not.djinni.presentation.navigation.NavigationController
import not.djinni.presentation.screens.public.main.PublicMainScreen
import not.djinni.presentation.screens.public.vacancy.details.PublicVacancyDetailsScreen

fun EntryProviderScope<Screens>.publicEntry(
    controller: NavigationController,
) {
    entry<Screens.Public.Main> {
        PublicMainScreen(
            onVacancyClick = { vacancyId ->
                controller.navigate(Screens.Public.VacancyDetails(vacancyId = vacancyId))
            },
            onLoginClick = { controller.replaceAll(Screens.Auth) },
        )
    }
    entry<Screens.Public.VacancyDetails> { entry ->
        PublicVacancyDetailsScreen(
            vacancyId = entry.vacancyId,
            onNavigateBack = { controller.popBackStack() },
        )
    }
}
