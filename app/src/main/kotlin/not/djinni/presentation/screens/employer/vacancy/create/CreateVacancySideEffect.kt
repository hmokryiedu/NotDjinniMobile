package not.djinni.presentation.screens.employer.createvacancy

internal sealed interface CreateVacancySideEffect {
    data object NavigateBack : CreateVacancySideEffect
}
