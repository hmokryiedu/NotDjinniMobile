package not.djinni.presentation.screens.seeker.application.details

import androidx.compose.runtime.Immutable
import not.djinni.model.application.ApplicationDetails
import not.djinni.presentation.core.components.base.model.TextData

@Immutable
internal data class ApplicationDetailsState(
    val contentState: ContentState = ContentState.Loading,
)

@Immutable
sealed interface ContentState {
    data object Loading : ContentState
    data class Error(val message: TextData) : ContentState
    data class Data(val application: ApplicationDetails) : ContentState
}
