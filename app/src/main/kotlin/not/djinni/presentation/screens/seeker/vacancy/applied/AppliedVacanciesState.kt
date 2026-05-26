package not.djinni.presentation.screens.seeker.vacancy.applied

import androidx.compose.runtime.Immutable
import not.djinni.model.application.ApplicationStatus
import not.djinni.presentation.core.components.base.model.TextData
import not.djinni.presentation.core.components.base.model.VacancyCardData

@Immutable
internal data class AppliedVacanciesState(
    val contentState: AppliedVacanciesContentState = AppliedVacanciesContentState.Loading,
    val selectedStatuses: Set<ApplicationStatus> = emptySet(),
    val draftStatuses: Set<ApplicationStatus> = emptySet(),
    val isStatusFilterVisible: Boolean = false,
)

@Immutable
internal sealed interface AppliedVacanciesContentState {
    data object Loading : AppliedVacanciesContentState
    data object Empty : AppliedVacanciesContentState
    data class Error(val message: TextData) : AppliedVacanciesContentState
    data class Data(val vacancies: List<AppliedVacancyCardData>) : AppliedVacanciesContentState
}

@Immutable
internal data class AppliedVacancyCardData(
    val vacancy: VacancyCardData,
    val status: ApplicationStatus,
)
