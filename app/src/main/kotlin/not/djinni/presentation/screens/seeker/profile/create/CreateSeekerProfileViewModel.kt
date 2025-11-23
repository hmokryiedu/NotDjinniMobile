package not.djinni.presentation.screens.seeker.profile.create

import kotlinx.coroutines.flow.asSharedFlow
import not.djinni.core.extension.mutableSideEffect
import not.djinni.presentation.core.StateViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class CreateSeekerProfileViewModel : StateViewModel<CreateSeekerProfileState>(
    CreateSeekerProfileState()
) {

    private val _sideEffect = mutableSideEffect<CreateSeekerProfileSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun sendAction(action: CreateSeekerProfileAction) {
        when (action) {
            is CreateSeekerProfileAction.InitializeProfile -> initializeProfile()
        }
    }

    private fun initializeProfile() {
        updateState { copy(profileInitialized = true) }
    }
}
