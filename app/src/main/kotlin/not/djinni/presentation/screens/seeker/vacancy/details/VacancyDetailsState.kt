package not.djinni.presentation.screens.seeker.vacancy.details

import androidx.compose.runtime.Immutable
import not.djinni.presentation.core.components.base.model.TextData
import not.djinni.presentation.screens.seeker.vacancy.details.alert.VacancyDetailsAlert

@Immutable
internal data class VacancyDetailsState(
    val contentState: VacancyDetailsContentState = VacancyDetailsContentState.Loading,
    val isApplied: Boolean = false,
    val selectedCoverLetterTemplate: String? = null,
    val currentAlert: VacancyDetailsAlert? = null,
)

internal sealed interface VacancyDetailsContentState {
    data object Loading : VacancyDetailsContentState
    data class Error(val message: TextData) : VacancyDetailsContentState
    data class Data(
        val vacancy: VacancyDisplayData,
        val eligibility: EligibilityState?,
    ) : VacancyDetailsContentState
}

@Immutable
internal data class VacancyDisplayData(
    val id: Long,
    val title: TextData,
    val companyName: TextData,
    val companyDescription: TextData?,
    val description: TextData,
    val viewsCount: TextData,
    val applicationsCount: TextData,
    val salaryRange: TextData,
    val employmentType: TextData,
    val requiredExperience: TextData,
    val category: TextData?,
    val postedDate: TextData,
    val isFavorite: Boolean,
)

@Immutable
internal data class EligibilityState(
    val canApply: Boolean,
    val experienceMatch: Boolean,
    val salaryMatch: Boolean,
    val salaryHint: TextData?,
)
