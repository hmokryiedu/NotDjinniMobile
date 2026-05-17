package not.djinni.presentation.screens.seeker.vacancy.details

internal sealed interface VacancyDetailsSideEffect {
    data object NavigateBack : VacancyDetailsSideEffect
    data object ApplicationSuccess : VacancyDetailsSideEffect
    data class NavigateToCoverLetterTemplates(val resultKeyId: String) : VacancyDetailsSideEffect
    data class NavigateToApplicationDetails(val applicationId: Long) : VacancyDetailsSideEffect
}
