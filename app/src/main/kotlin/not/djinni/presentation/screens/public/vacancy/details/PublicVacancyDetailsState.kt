package not.djinni.presentation.screens.public.vacancy.details

import androidx.compose.runtime.Immutable
import not.djinni.presentation.core.components.base.model.TextData

@Immutable
internal data class PublicVacancyDetailsState(
    val contentState: PublicVacancyDetailsContentState = PublicVacancyDetailsContentState.Loading,
)

internal sealed interface PublicVacancyDetailsContentState {
    data object Loading : PublicVacancyDetailsContentState
    data class Error(val message: TextData) : PublicVacancyDetailsContentState
    data class Data(val vacancy: PublicVacancyDisplayData) : PublicVacancyDetailsContentState
}

@Immutable
internal data class PublicVacancyDisplayData(
    val id: Long,
    val title: TextData,
    val companyName: TextData,
    val companyDescription: TextData?,
    val description: TextData,
    val salaryRange: TextData,
    val employmentType: TextData,
    val requiredExperience: TextData,
    val category: TextData?,
    val postedDate: TextData,
)
