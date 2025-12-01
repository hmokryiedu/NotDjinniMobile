package not.djinni.presentation.screens.employer.profile.create

import not.djinni.model.company.Company

internal sealed interface CreateEmployerProfileAction {
    data object ShowSelectCompanyAlert : CreateEmployerProfileAction
    data object HideAlert : CreateEmployerProfileAction
    data class SearchCompanies(val query: String) : CreateEmployerProfileAction
    data class SelectCompany(val company: Company) : CreateEmployerProfileAction
    data class CreateProfile(val role: String) : CreateEmployerProfileAction
}
