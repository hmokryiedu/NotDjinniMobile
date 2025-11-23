package not.djinni.presentation.screens.seeker.allvacancies

import kotlinx.coroutines.flow.asSharedFlow
import not.djinni.core.extension.mutableSideEffect
import not.djinni.presentation.core.StateViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class AllVacanciesViewModel : StateViewModel<AllVacanciesState>(
    AllVacanciesState()
) {

    private val _sideEffect = mutableSideEffect<AllVacanciesSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun sendAction(action: AllVacanciesAction) {

    }
}
