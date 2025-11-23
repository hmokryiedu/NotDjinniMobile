package not.djinni.presentation.screens.employer

import not.djinni.presentation.core.StateViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class MainEmployerViewModel : StateViewModel<MainEmployerState>(MainEmployerState()) {

    fun sendAction(action: MainEmployerAction) {
    }
}
