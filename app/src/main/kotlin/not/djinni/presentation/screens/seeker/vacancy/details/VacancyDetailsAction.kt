package not.djinni.presentation.screens.seeker.vacancy.details

internal sealed interface VacancyDetailsAction {
    data object Load : VacancyDetailsAction
    data object Apply : VacancyDetailsAction
    data object SeeApplication : VacancyDetailsAction
    data object NavigateBack : VacancyDetailsAction
    data object ToggleFavorite : VacancyDetailsAction
    data object HideApplyBottomSheet : VacancyDetailsAction
    data class OpenCoverLetterTemplates(val resultKeyId: String) : VacancyDetailsAction
    data class ApplyCoverLetterTemplate(val coverLetter: String) : VacancyDetailsAction
    data class SubmitApplication(val coverLetter: String?) : VacancyDetailsAction
}
