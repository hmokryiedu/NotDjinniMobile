package not.djinni.presentation.screens.seeker.coverletter.templates

internal sealed interface CoverLetterTemplatesAction {
    data object Load : CoverLetterTemplatesAction
    data object NavigateBack : CoverLetterTemplatesAction
    data object CreateTemplate : CoverLetterTemplatesAction
    data class OpenTemplate(val id: Long) : CoverLetterTemplatesAction
    data object StartEdit : CoverLetterTemplatesAction
    data class UpdateEditingMessage(val message: String) : CoverLetterTemplatesAction
    data object Save : CoverLetterTemplatesAction
    data class RequestDelete(val id: Long) : CoverLetterTemplatesAction
    data object ConfirmDelete : CoverLetterTemplatesAction
    data object DismissDeleteConfirmation : CoverLetterTemplatesAction
    data object Apply : CoverLetterTemplatesAction
    data object DismissDialog : CoverLetterTemplatesAction
}
