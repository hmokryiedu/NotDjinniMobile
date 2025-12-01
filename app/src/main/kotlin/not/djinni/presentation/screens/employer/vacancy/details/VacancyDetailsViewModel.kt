package not.djinni.presentation.screens.employer.vacancy.details

import kotlinx.coroutines.flow.asSharedFlow
import not.djinni.core.extension.mutableSideEffect
import not.djinni.presentation.core.StateViewModel
import not.djinni.presentation.screens.employer.vacancydetails.VacancyDetailsAction
import not.djinni.presentation.screens.employer.vacancydetails.VacancyDetailsSideEffect
import not.djinni.presentation.screens.employer.vacancydetails.VacancyDetailsState
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam

@KoinViewModel
internal class VacancyDetailsViewModel(
    @InjectedParam private val vacancyId: Long,
) : StateViewModel<VacancyDetailsState>(
    VacancyDetailsState()
) {
    private val _sideEffect = mutableSideEffect<VacancyDetailsSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun sendAction(action: VacancyDetailsAction) {

    }
}
