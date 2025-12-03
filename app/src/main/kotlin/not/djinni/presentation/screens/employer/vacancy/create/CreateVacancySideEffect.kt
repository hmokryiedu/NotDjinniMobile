package not.djinni.presentation.screens.employer.vacancy.create

internal sealed interface CreateVacancySideEffect {
    data object NavigateBack : CreateVacancySideEffect
    data class NavigateToDetails(val vacancyId: Long) : CreateVacancySideEffect
}
