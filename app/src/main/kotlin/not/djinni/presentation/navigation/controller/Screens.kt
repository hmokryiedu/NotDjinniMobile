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
    sealed interface Seeker : Screens {

        @Serializable
        data object Main : Seeker

        @Serializable
        data object AllVacancies : Seeker

        @Serializable
        data object CreateProfile : Seeker

        @Serializable
        data object VacancyDetails : Seeker

        @Serializable
        data object AppliedVacancies : Seeker
    }

    @Serializable
    sealed interface Employer : Screens {

        @Serializable
        data object Main : Seeker

        @Serializable
        data object CreateProfile : Seeker

        @Serializable
        data object ViewProfile : Seeker

        @Serializable
        data object VacancyDetails : Seeker

        @Serializable
        data object CreateVacancy : Seeker
    }
}
