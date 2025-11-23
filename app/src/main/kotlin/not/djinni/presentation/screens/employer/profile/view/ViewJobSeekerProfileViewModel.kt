package not.djinni.presentation.screens.employer.viewjobseekerprofile

import kotlinx.coroutines.flow.asSharedFlow
import not.djinni.core.extension.mutableSideEffect
import not.djinni.presentation.core.StateViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ViewJobSeekerProfileViewModel : StateViewModel<ViewJobSeekerProfileState>(
    ViewJobSeekerProfileState()
) {

    private val _sideEffect = mutableSideEffect<ViewJobSeekerProfileSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun sendAction(action: ViewJobSeekerProfileAction) {

    }
}
