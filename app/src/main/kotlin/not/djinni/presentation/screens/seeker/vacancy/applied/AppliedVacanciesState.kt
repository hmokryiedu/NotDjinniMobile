package not.djinni.presentation.screens.seeker.vacancy.applied

import androidx.compose.runtime.Immutable
import not.djinni.presentation.core.components.base.model.TextData
import not.djinni.presentation.core.components.base.model.VacancyCardData

@Immutable
internal data class AppliedVacanciesState(
    val contentState: AppliedVacanciesContentState = AppliedVacanciesContentState.Loading,
)

@Immutable
internal sealed interface AppliedVacanciesContentState {
    data object Loading : AppliedVacanciesContentState
    data object Empty : AppliedVacanciesContentState
    data class Error(val message: TextData) : AppliedVacanciesContentState
    data class Data(val vacancies: List<VacancyCardData>) : AppliedVacanciesContentState
}
