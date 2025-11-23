package not.djinni.presentation.screens.seeker.appliedvacancies

import kotlinx.coroutines.flow.asSharedFlow
import not.djinni.core.extension.mutableSideEffect
import not.djinni.presentation.core.StateViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class AppliedVacanciesViewModel : StateViewModel<AppliedVacanciesState>(
    AppliedVacanciesState()
) {

    private val _sideEffect = mutableSideEffect<AppliedVacanciesSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun sendAction(action: AppliedVacanciesAction) {

    }
}
