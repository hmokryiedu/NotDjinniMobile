package not.djinni.presentation.navigation.controller

import androidx.navigation3.runtime.EntryProviderScope
import not.djinni.presentation.navigation.NavigationController
import not.djinni.presentation.screens.employer.createvacancy.CreateVacancyScreen
import not.djinni.presentation.screens.employer.main.MainEmployerScreen
import not.djinni.presentation.screens.employer.profile.create.CreateEmployerProfileScreen
import not.djinni.presentation.screens.employer.profile.view.ViewJobSeekerProfileScreen
import not.djinni.presentation.screens.employer.vacancy.details.VacancyDetailsScreen

fun EntryProviderScope<Screens>.employerEntry(
    controller: NavigationController,
) {
    entry<Screens.Employer.CreateProfile> {
        CreateEmployerProfileScreen(
            onHome = { controller.replaceAll(Screens.Employer.Main) }
        )
    }
    entry<Screens.Employer.Main> {
        MainEmployerScreen(
            onVacancyClick = { vacancyId ->
                controller.navigate(Screens.Employer.VacancyDetails(vacancyId = vacancyId))
            }
        )
    }
    entry<Screens.Employer.VacancyDetails> { entry ->
        VacancyDetailsScreen(
            vacancyId = entry.vacancyId,
            onNavigateBack = { controller.popBackStack() }
        )
    }
    entry<Screens.Employer.CreateVacancy> {
        CreateVacancyScreen(onNavigateBack = { controller.popBackStack() })
    }
    entry<Screens.Employer.ViewProfile> {
        ViewJobSeekerProfileScreen()
    }
}