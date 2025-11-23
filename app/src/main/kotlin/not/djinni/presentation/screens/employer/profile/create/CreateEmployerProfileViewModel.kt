package not.djinni.presentation.screens.employer

import kotlinx.coroutines.flow.asSharedFlow
import not.djinni.core.extension.mutableSideEffect
import not.djinni.presentation.core.StateViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class CreateEmployerProfileViewModel : StateViewModel<CreateEmployerProfileState>(
    CreateEmployerProfileState()
) {

    private val _sideEffect = mutableSideEffect<CreateEmployerProfileSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun sendAction(action: CreateEmployerProfileAction) {
        when (action) {
            is CreateEmployerProfileAction.InitializeProfile -> initializeProfile()
        }
    }

    private fun initializeProfile() {
        updateState { copy(profileInitialized = true) }
    }
}
