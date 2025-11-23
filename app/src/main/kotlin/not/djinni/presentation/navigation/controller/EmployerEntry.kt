package not.djinni.presentation.navigation.controller

import androidx.navigation3.runtime.EntryProviderScope
import not.djinni.presentation.navigation.NavigationController
import not.djinni.presentation.screens.employer.MainEmployerScreen
import not.djinni.presentation.screens.employer.createvacancy.CreateVacancyScreen
import not.djinni.presentation.screens.employer.profile.create.CreateEmployerProfileScreen
import not.djinni.presentation.screens.employer.profile.view.ViewJobSeekerProfileScreen
import not.djinni.presentation.screens.employer.vacancydetails.VacancyDetailsScreen

fun EntryProviderScope<Screens>.employerEntry(
    controller: NavigationController,
) {
    entry<Screens.Employer.CreateProfile> {
        CreateEmployerProfileScreen(
            onHome = { controller.replaceAll(Screens.Seeker.Main) }
        )
    }
    entry<Screens.Employer.Main> {
        MainEmployerScreen()
    }
    entry<Screens.Employer.VacancyDetails> {
        VacancyDetailsScreen()
    }
    entry<Screens.Employer.CreateVacancy> {
        CreateVacancyScreen(onNavigateBack = {})
    }
    entry<Screens.Employer.ViewProfile> {
        ViewJobSeekerProfileScreen()
    }
}