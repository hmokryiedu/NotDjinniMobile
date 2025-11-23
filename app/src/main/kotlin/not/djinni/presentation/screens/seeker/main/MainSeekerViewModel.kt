package not.djinni.presentation.screens.seeker.main

import kotlinx.coroutines.flow.asSharedFlow
import not.djinni.core.extension.mutableSideEffect
import not.djinni.presentation.core.StateViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class MainSeekerViewModel : StateViewModel<MainSeekerState>(
    MainSeekerState()
) {

    private val _sideEffect = mutableSideEffect<MainSeekerSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun sendAction(action: MainSeekerAction) {

    }
}
