package not.djinni.presentation.screens.employer.profile.create

import androidx.compose.runtime.Immutable
import not.djinni.model.company.Company
import not.djinni.presentation.core.components.base.model.TextData
import not.djinni.presentation.screens.employer.profile.create.alert.CreateEmployerProfileAlert

@Immutable
internal data class CreateEmployerProfileState(
    val message: TextData? = null,
    val currentAlert: CreateEmployerProfileAlert? = null,
    val selectedCompany: Company? = null,
    val searchResults: List<Company> = emptyList(),
    val isSearching: Boolean = false,
)
