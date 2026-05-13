package not.djinni.presentation.navigation.controller

import androidx.navigation3.runtime.EntryProviderScope
import not.djinni.presentation.navigation.NavigationController
import not.djinni.presentation.screens.employer.application.details.ApplicationDetailsScreen
import not.djinni.presentation.screens.employer.main.MainEmployerScreen
import not.djinni.presentation.screens.employer.profile.create.CreateEmployerProfileScreen
import not.djinni.presentation.screens.employer.profile.my.EmployerProfileScreen
import not.djinni.presentation.screens.employer.profile.view.ViewJobSeekerProfileScreen
import not.djinni.presentation.screens.employer.vacancy.applications.ViewVacancyApplicationsScreen
import not.djinni.presentation.screens.employer.vacancy.create.CreateVacancyScreen
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
            },
            onProfileClick = { controller.navigate(Screens.Employer.Profile) },
            onCreateVacancyClick = { controller.navigate(Screens.Employer.CreateVacancy) }
        )
    }
    entry<Screens.Employer.VacancyDetails> { entry ->
        VacancyDetailsScreen(
            vacancyId = entry.vacancyId,
            onNavigateBack = { controller.popBackStack() },
            onNavigateToApplications = { vacancyId ->
                controller.navigate(Screens.Employer.VacancyApplications(vacancyId = vacancyId))
            }
        )
    }
    entry<Screens.Employer.VacancyApplications> { entry ->
        ViewVacancyApplicationsScreen(
            vacancyId = entry.vacancyId,
            onNavigateBack = { controller.popBackStack() },
            onNavigateToApplicationDetails = { applicationId ->
                controller.navigate(Screens.Employer.ViewApplicationDetails(applicationId = applicationId))
            }
        )
    }
    entry<Screens.Employer.ViewApplicationDetails> { entry ->
        ApplicationDetailsScreen(
            applicationId = entry.applicationId,
            onNavigateBack = { controller.popBackStack() },
            onNavigateToVacancyDetails = { vacancyId ->
                controller.navigate(Screens.Employer.VacancyDetails(vacancyId = vacancyId))
            }
        )
    }
    entry<Screens.Employer.CreateVacancy> {
        CreateVacancyScreen(
            onNavigateBack = { controller.popBackStack() },
            onNavigateToDetails = { vacancyId ->
                controller.popUpTo(
                    key = Screens.Employer.VacancyDetails(vacancyId),
                    to = Screens.Employer.Main,
                )
            }
        )
    }
    entry<Screens.Employer.ViewProfile> {
        ViewJobSeekerProfileScreen()
    }
    entry<Screens.Employer.Profile> {
        EmployerProfileScreen(
            onChangeRole = { controller.replaceAll(Screens.ChooseRole) },
            onLogout = { controller.replaceAll(Screens.Auth) }
        )
    }
}
