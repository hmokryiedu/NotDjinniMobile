package not.djinni.presentation.screens.seeker.application.list

import androidx.compose.runtime.Immutable
import not.djinni.model.application.ApplicationDetails
import not.djinni.presentation.core.components.base.model.TextData

@Immutable
internal data class ViewApplicationsState(
    val contentState: ContentState = ContentState.Loading,
)

@Immutable
sealed interface ContentState {
    data object Loading : ContentState
    data class Error(val message: TextData) : ContentState
    data class Data(val applications: List<ApplicationDetails>) : ContentState

    fun items(): List<ApplicationDetails> = when (this) {
        is Loading -> emptyList()
        is Error -> emptyList()
        is Data -> applications
    }
}
