package not.djinni.presentation.screens.employer.vacancy.applications

import androidx.compose.runtime.Immutable
import not.djinni.presentation.core.components.base.model.ApplicationCardData
import not.djinni.presentation.core.components.base.model.TextData

@Immutable
internal data class ViewVacancyApplicationsState(
    val contentState: ApplicationsContentState = ApplicationsContentState.Loading,
)

internal sealed interface ApplicationsContentState {
    data object Loading : ApplicationsContentState
    data class Error(val message: TextData) : ApplicationsContentState
    data object Empty : ApplicationsContentState
    data class Data(val applications: List<ApplicationCardData>) : ApplicationsContentState

    val items: List<ApplicationCardData>
        get() = when (this) {
            is Data -> applications
            else -> emptyList()
        }
}
