package not.djinni.presentation.screens.seeker.coverletter.templates

import androidx.compose.runtime.Immutable

@Immutable
internal data class CoverLetterTemplatesState(
    val contentState: CoverLetterTemplatesContentState = CoverLetterTemplatesContentState.Loading,
    val selectedTemplate: CoverLetterTemplateDisplayData? = null,
    val isEditing: Boolean = false,
    val editingMessage: String = "",
)

@Immutable
internal sealed interface CoverLetterTemplatesContentState {
    data object Loading : CoverLetterTemplatesContentState
    data object Empty : CoverLetterTemplatesContentState
    data class Error(val message: String) : CoverLetterTemplatesContentState
    data class Data(val templates: List<CoverLetterTemplateDisplayData>) : CoverLetterTemplatesContentState
}

@Immutable
internal data class CoverLetterTemplateDisplayData(
    val id: Long,
    val message: String,
)
