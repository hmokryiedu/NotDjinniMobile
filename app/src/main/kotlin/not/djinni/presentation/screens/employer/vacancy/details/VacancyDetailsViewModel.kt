package not.djinni.presentation.screens.employer.vacancydetails

import kotlinx.coroutines.flow.asSharedFlow
import not.djinni.core.extension.mutableSideEffect
import not.djinni.presentation.core.StateViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class VacancyDetailsViewModel : StateViewModel<VacancyDetailsState>(
    VacancyDetailsState()
) {

    private val _sideEffect = mutableSideEffect<VacancyDetailsSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun sendAction(action: VacancyDetailsAction) {

    }
}
