package not.djinni.presentation.navigation.controller

import androidx.navigation3.runtime.EntryProviderScope
import not.djinni.presentation.navigation.NavigationController
import not.djinni.presentation.screens.seeker.coverletter.templates.CoverLetterTemplatesScreen
import not.djinni.presentation.screens.seeker.application.details.ApplicationDetailsScreen
import not.djinni.presentation.screens.seeker.application.list.ViewApplicationsScreen
import not.djinni.presentation.screens.seeker.main.MainSeekerScreen
import not.djinni.presentation.screens.seeker.profile.create.CreateSeekerProfileScreen
import not.djinni.presentation.screens.seeker.profile.edit.EditSeekerProfileScreen
import not.djinni.presentation.screens.seeker.profile.view.SeekerProfileScreen
import not.djinni.presentation.screens.seeker.vacancy.all.AllVacanciesScreen
import not.djinni.presentation.screens.seeker.vacancy.applied.AppliedVacanciesScreen
import not.djinni.presentation.screens.seeker.vacancy.details.VacancyDetailsScreen
import not.djinni.presentation.screens.seeker.vacancy.favorite.FavoriteVacanciesScreen

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
            onProfileClick = { controller.navigate(Screens.Seeker.Profile) },
            onApplicationsClick = { controller.navigate(Screens.Seeker.AppliedVacancies) },
            onFavoriteVacanciesClick = { controller.navigate(Screens.Seeker.FavoriteVacancies) }
        )
    }
    entry<Screens.Seeker.AllVacancies> {
        AllVacanciesScreen()
    }
    entry<Screens.Seeker.AppliedVacancies> {
        AppliedVacanciesScreen(
            onBack = { controller.popBackStack() },
            onVacancyClick = { vacancyId ->
                controller.navigate(Screens.Seeker.VacancyDetails(vacancyId = vacancyId))
            }
        )
    }
    entry<Screens.Seeker.FavoriteVacancies> {
        FavoriteVacanciesScreen(
            onBack = { controller.popBackStack() },
            onVacancyClick = { vacancyId ->
                controller.navigate(Screens.Seeker.VacancyDetails(vacancyId = vacancyId))
            }
        )
    }
    entry<Screens.Seeker.VacancyDetails> { entry ->
        VacancyDetailsScreen(
            vacancyId = entry.vacancyId,
            onNavigateBack = { controller.popBackStack() },
            onNavigateToCoverLetterTemplates = { vacancyId ->
                controller.navigate(
                    Screens.Seeker.CoverLetterTemplates(
                        vacancyId = vacancyId,
                        resultKey = coverLetterResultKey(vacancyId)
                    )
                )
            },
            onNavigateToApplicationDetails = { applicationId ->
                controller.navigate(Screens.Seeker.ApplicationDetails(applicationId = applicationId))
            }
        )
    }
    entry<Screens.Seeker.CoverLetterTemplates> { entry ->
        CoverLetterTemplatesScreen(
            resultKey = entry.resultKey,
            onBack = { controller.popBackStack() },
        )
    }
    entry<Screens.Seeker.ViewApplications> {
        ViewApplicationsScreen(
            onBack = { controller.popBackStack() },
            onNavigateToDetails = { applicationId ->
                controller.navigate(Screens.Seeker.ApplicationDetails(applicationId = applicationId))
            }
        )
    }
    entry<Screens.Seeker.ApplicationDetails> { entry ->
        ApplicationDetailsScreen(
            applicationId = entry.applicationId,
            onNavigateBack = { controller.popBackStack() }
        )
    }
    entry<Screens.Seeker.Profile> {
        SeekerProfileScreen(
            onBack = { controller.popBackStack() },
            onChangeRole = { controller.replaceAll(Screens.ChooseRole) },
            onEditProfile = { controller.navigate(Screens.Seeker.EditProfile) },
            onLogout = { controller.replaceAll(Screens.Auth) },
        )
    }
    entry<Screens.Seeker.EditProfile> {
        EditSeekerProfileScreen(
            onBack = { controller.popBackStack() }
        )
    }
}

private fun coverLetterResultKey(vacancyId: Long): String = "cover_letter_result_$vacancyId"
