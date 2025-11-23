package not.djinni.presentation.screens.seeker.vacancydetails

import androidx.compose.runtime.Immutable

@Immutable
internal data class VacancyDetailsState(
    val vacancyId: String = "",
    val isLoading: Boolean = false,
)
