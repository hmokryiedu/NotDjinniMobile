package not.djinni.presentation.screens.public.vacancy.details

internal sealed interface PublicVacancyDetailsSideEffect {
    data object NavigateBack : PublicVacancyDetailsSideEffect
}
