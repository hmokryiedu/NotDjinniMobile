package not.djinni.presentation.screens.seeker.vacancy.all

import kotlinx.coroutines.flow.asSharedFlow
import not.djinni.core.extension.mutableSideEffect
import not.djinni.presentation.core.StateViewModel
import not.djinni.presentation.screens.seeker.allvacancies.AllVacanciesAction
import not.djinni.presentation.screens.seeker.allvacancies.AllVacanciesSideEffect
import not.djinni.presentation.screens.seeker.allvacancies.AllVacanciesState
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
