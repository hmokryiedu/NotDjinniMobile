package not.djinni.presentation.screens.seeker.coverletter.templates

internal sealed interface CoverLetterTemplatesSideEffect {
    data object NavigateBack : CoverLetterTemplatesSideEffect
    data class ApplyTemplate(val message: String) : CoverLetterTemplatesSideEffect
}
