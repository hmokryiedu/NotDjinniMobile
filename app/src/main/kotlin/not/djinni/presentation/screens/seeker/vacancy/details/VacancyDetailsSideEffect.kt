package not.djinni.presentation.screens.seeker.vacancy.details

internal sealed interface VacancyDetailsSideEffect {
    data object NavigateBack : VacancyDetailsSideEffect
    data object ApplicationSuccess : VacancyDetailsSideEffect
}
