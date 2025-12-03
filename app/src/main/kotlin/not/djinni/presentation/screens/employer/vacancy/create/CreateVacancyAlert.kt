package not.djinni.presentation.screens.employer.vacancy.create

internal sealed interface CreateVacancyAlert {
    data object SelectEmploymentType : CreateVacancyAlert
    data object SelectCategory : CreateVacancyAlert
}
