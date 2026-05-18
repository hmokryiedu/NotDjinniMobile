package not.djinni.presentation.navigation.controller

import androidx.navigation3.runtime.EntryProviderScope
import not.djinni.presentation.navigation.NavigationController
import not.djinni.presentation.navigation.NavResultKey
import not.djinni.presentation.screens.seeker.coverletter.templates.CoverLetterTemplatesScreen
import not.djinni.presentation.screens.seeker.application.details.ApplicationDetailsScreen
import not.djinni.presentation.screens.seeker.application.list.ViewApplicationsScreen
import not.djinni.presentation.screens.seeker.main.MainSeekerScreen
import not.djinni.presentation.screens.seeker.profile.create.CreateSeekerProfileScreen
import not.djinni.presentation.screens.seeker.profile.edit.EditSeekerProfileScreen
import not.djinni.presentation.screens.seeker.profile.edit.ProfileEditResultContract
import not.djinni.presentation.screens.seeker.profile.view.SeekerProfileScreen
import not.djinni.presentation.screens.seeker.vacancy.all.AllVacanciesScreen
import not.djinni.presentation.screens.seeker.vacancy.details.coverletter.CoverLetterResultContract
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
            onBack = controller::popBackStack,
            onVacancyClick = { vacancyId ->
                controller.navigate(Screens.Seeker.VacancyDetails(vacancyId = vacancyId))
            }
        )
    }
    entry<Screens.Seeker.FavoriteVacancies> {
        FavoriteVacanciesScreen(
            onBack = controller::popBackStack,
            onVacancyClick = { vacancyId ->
                controller.navigate(Screens.Seeker.VacancyDetails(vacancyId = vacancyId))
            }
        )
    }
    entry<Screens.Seeker.VacancyDetails> { entry ->
        val coverLetterResultKey = NavResultKey(
            id = "cover_letter_result_${entry.vacancyId}",
            contract = CoverLetterResultContract
        )
        VacancyDetailsScreen(
            vacancyId = entry.vacancyId,
            onNavigateBack = controller::popBackStack,
            onNavigateToCoverLetterTemplates = { vacancyId, resultKey ->
                controller.navigateForResult(
                    Screens.Seeker.CoverLetterTemplates(
                        vacancyId = vacancyId,
                        resultKeyId = resultKey.id
                    ),
                    resultKey
                )
            },
            coverLetterResult = controller.consumeResult(coverLetterResultKey),
            onNavigateToApplicationDetails = { applicationId ->
                controller.navigate(Screens.Seeker.ApplicationDetails(applicationId = applicationId))
            }
        )
    }
    entry<Screens.Seeker.CoverLetterTemplates> { entry ->
        val resultKey = NavResultKey(
            id = entry.resultKeyId,
            contract = CoverLetterResultContract
        )
        CoverLetterTemplatesScreen(
            onBack = controller::popBackStack,
            onApply = { message ->
                controller.popWithResult(resultKey, message)
            }
        )
    }
    entry<Screens.Seeker.ViewApplications> {
        ViewApplicationsScreen(
            onBack = controller::popBackStack,
            onNavigateToDetails = { applicationId ->
                controller.navigate(Screens.Seeker.ApplicationDetails(applicationId = applicationId))
            }
        )
    }
    entry<Screens.Seeker.ApplicationDetails> { entry ->
        ApplicationDetailsScreen(
            applicationId = entry.applicationId,
            onNavigateBack = controller::popBackStack
        )
    }
    entry<Screens.Seeker.Profile> {
        val editResultKey = NavResultKey(
            id = "seeker_profile_edited",
            contract = ProfileEditResultContract
        )
        SeekerProfileScreen(
            onBack = controller::popBackStack,
            onChangeRole = { controller.replaceAll(Screens.ChooseRole) },
            onEditProfile = { controller.navigateForResult(Screens.Seeker.EditProfile, editResultKey) },
            onLogout = { controller.replaceAll(Screens.Auth) },
            isProfileUpdated = controller.consumeResult(editResultKey) == true,
        )
    }
    entry<Screens.Seeker.EditProfile> {
        val resultKey = NavResultKey(
            id = "seeker_profile_edited",
            contract = ProfileEditResultContract
        )
        EditSeekerProfileScreen(
            onBack = { controller.popWithResult(resultKey, true) }
        )
    }
}
