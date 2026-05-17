package not.djinni.presentation.navigation.controller

import androidx.compose.runtime.Stable
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Stable
@Serializable
sealed interface Screens : NavKey {

    @Serializable
    data object Splash : Screens

    @Serializable
    data object Auth : Screens

    @Serializable
    data object ChooseRole : Screens

    @Serializable
    sealed interface Public : Screens {

        @Serializable
        data object Main : Public

        @Serializable
        data class VacancyDetails(val vacancyId: Long) : Public
    }

    @Serializable
    sealed interface Seeker : Screens {

        @Serializable
        data object Main : Seeker

        @Serializable
        data object AllVacancies : Seeker

        @Serializable
        data object CreateProfile : Seeker

        @Serializable
        data class VacancyDetails(val vacancyId: Long) : Seeker

        @Serializable
        data object AppliedVacancies : Seeker

        @Serializable
        data object FavoriteVacancies : Seeker

        @Serializable
        data object ViewApplications : Seeker

        @Serializable
        data class ApplicationDetails(val applicationId: Long) : Seeker

        @Serializable
        data object Profile : Seeker
    }

    @Serializable
    sealed interface Employer : Screens {

        @Serializable
        data object Main : Employer

        @Serializable
        data object CreateProfile : Employer

        @Serializable
        data object ViewProfile : Employer

        @Serializable
        data class VacancyDetails(val vacancyId: Long) : Employer

        @Serializable
        data class VacancyApplications(val vacancyId: Long) : Employer

        @Serializable
        data class ViewApplicationDetails(val applicationId: Long) : Employer

        @Serializable
        data object CreateVacancy : Employer

        @Serializable
        data object Profile : Employer
    }
}
