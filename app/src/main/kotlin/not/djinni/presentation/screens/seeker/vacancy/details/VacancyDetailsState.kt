package not.djinni.presentation.screens.seeker.vacancy.details

import androidx.compose.runtime.Immutable
import not.djinni.model.seeker.SeekerProfile
import not.djinni.model.seeker.vacancy.Vacancy
import not.djinni.model.seeker.vacancy.VacancyStatusCode
import not.djinni.presentation.core.components.base.model.TextData
import not.djinni.presentation.core.extension.toTextData
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
    val blockers: List<TextData>,
    val warnings: List<TextData>,
    val salaryHint: TextData?,
)

internal fun EligibilityState?.isApplyAvailable(): Boolean = this?.canApply == true && this.blockers.isEmpty()

internal fun calculateEligibility(
    vacancy: Vacancy,
    profile: SeekerProfile,
    salaryHintProvider: (Int) -> String,
    missingCategoryWarningProvider: () -> String,
    categoryMismatchBlockerProvider: () -> String,
    inactiveVacancyBlockerProvider: () -> String,
    insufficientExperienceBlockerProvider: (Int) -> String,
): EligibilityState {
    val blockers = mutableListOf<TextData>()
    val warnings = mutableListOf<TextData>()

    if (vacancy.status != VacancyStatusCode.ACTIVE) {
        blockers += inactiveVacancyBlockerProvider().toTextData()
    }

    if (vacancy.category == null) {
        warnings += missingCategoryWarningProvider().toTextData()
    } else if (profile.jobCategory != vacancy.category) {
        blockers += categoryMismatchBlockerProvider().toTextData()
    }

    val minExperience = vacancy.minExperienceYears
    if (minExperience != null && minExperience > 0 && profile.experienceYears < minExperience) {
        blockers += insufficientExperienceBlockerProvider(minExperience).toTextData()
    }

    val salaryMatch = vacancy.salaryMax >= profile.desiredSalary
    val salaryHint = salaryHintProvider(profile.desiredSalary)
        .toTextData()
        .takeIf { !salaryMatch }
    if (salaryHint != null) {
        warnings += salaryHint
    }

    return EligibilityState(
        canApply = blockers.isEmpty(),
        blockers = blockers,
        warnings = warnings,
        salaryHint = salaryHint
    )
}
