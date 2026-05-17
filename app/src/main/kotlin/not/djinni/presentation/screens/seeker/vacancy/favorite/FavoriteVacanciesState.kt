package not.djinni.presentation.screens.seeker.vacancy.favorite

import androidx.compose.runtime.Immutable
import not.djinni.presentation.core.components.base.model.TextData
import not.djinni.presentation.core.components.base.model.VacancyCardData

@Immutable
internal data class FavoriteVacanciesState(
    val contentState: FavoriteVacanciesContentState = FavoriteVacanciesContentState.Loading,
)

@Immutable
internal sealed interface FavoriteVacanciesContentState {
    data object Loading : FavoriteVacanciesContentState
    data object Empty : FavoriteVacanciesContentState
    data class Error(val message: TextData) : FavoriteVacanciesContentState
    data class Data(val vacancies: List<VacancyCardData>) : FavoriteVacanciesContentState
}
