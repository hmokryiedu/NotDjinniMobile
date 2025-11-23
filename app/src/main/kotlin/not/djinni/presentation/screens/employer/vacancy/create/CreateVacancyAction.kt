package not.djinni.presentation.screens.employer.createvacancy

internal sealed interface CreateVacancyAction {
    data object Initialize : CreateVacancyAction
}
