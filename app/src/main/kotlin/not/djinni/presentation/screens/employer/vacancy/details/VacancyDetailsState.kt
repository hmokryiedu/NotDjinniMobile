package not.djinni.presentation.screens.employer.vacancy.details

import androidx.compose.runtime.Immutable
import not.djinni.presentation.core.components.base.model.TextData

@Immutable
internal data class VacancyDetailsState(
    val contentState: VacancyDetailsContentState = VacancyDetailsContentState.Loading,
)

internal sealed interface VacancyDetailsContentState {
    data object Loading : VacancyDetailsContentState
    data class Error(val message: TextData) : VacancyDetailsContentState
    data class Data(val vacancy: VacancyDisplayData) : VacancyDetailsContentState
}

@Immutable
internal data class VacancyDisplayData(
    val id: Long,
    val title: TextData,
    val description: TextData,
    val salaryRange: TextData,
    val employmentType: TextData,
    val requiredExperience: TextData,
    val category: TextData?,
    val status: TextData,
    val postedDate: TextData,
    val updatedDate: TextData,
)
