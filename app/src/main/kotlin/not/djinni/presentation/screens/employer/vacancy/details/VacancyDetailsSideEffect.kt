package not.djinni.presentation.screens.employer.vacancy.details

internal sealed interface VacancyDetailsSideEffect {
    data object NavigateBack : VacancyDetailsSideEffect
}
