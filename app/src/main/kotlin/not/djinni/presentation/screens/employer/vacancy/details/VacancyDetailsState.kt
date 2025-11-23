package not.djinni.presentation.screens.employer.vacancydetails

import androidx.compose.runtime.Immutable

@Immutable
internal data class VacancyDetailsState(
    val vacancyId: String = "",
    val isLoading: Boolean = false,
)
