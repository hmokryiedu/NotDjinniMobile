package not.djinni.presentation.screens.employer.createvacancy

import kotlinx.coroutines.flow.asSharedFlow
import not.djinni.core.extension.mutableSideEffect
import not.djinni.presentation.core.StateViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class CreateVacancyViewModel : StateViewModel<CreateVacancyState>(
    CreateVacancyState()
) {

    private val _sideEffect = mutableSideEffect<CreateVacancySideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun sendAction(action: CreateVacancyAction) {
        when (action) {
            is CreateVacancyAction.Initialize -> initialize()
        }
    }

    private fun initialize() {
        updateState { copy(vacancyTitle = "") }
    }
}
