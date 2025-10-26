package not.djinni.presentation.screens.onboarding

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import not.djinni.core.extension.mutableSideEffect
import not.djinni.core.extension.orFalse
import not.djinni.presentation.core.StateViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class OnboardingViewModel : StateViewModel<OnboardingState>(OnboardingState()) {

    private val _sideEffect = mutableSideEffect<OnboardingSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private val personalInformation = MutableStateFlow<PersonalInformation?>(null)

    init {
        collectIsButtonEnabled()
    }

    fun onAction(action: OnboardingAction) {
        when (action) {
            is OnboardingAction.PersonalInformationChanged -> {
                setPersonalInformation(fullName = action.fullName, aboutMe = action.aboutMe)
            }

            OnboardingAction.Continue -> onContinue()
        }
    }

    private fun onContinue() {
        val step = when (state.value.step) {
            Step.PERSONAL_INFORMATION -> Step.JOB_PREFERENCES
            Step.JOB_PREFERENCES -> Step.COMPLETED
            Step.COMPLETED -> return
        }
        updateState { copy(step = step) }
    }

    private fun setPersonalInformation(fullName: String, aboutMe: String) {
        personalInformation.update {
            PersonalInformation(fullName = fullName, aboutMe = aboutMe)
        }
    }

    private fun collectIsButtonEnabled() {
        launch {
            combine(
                state,
                personalInformation
            ) { state, personalInformation ->
                when (state.step) {
                    Step.PERSONAL_INFORMATION -> personalInformation?.isValid().orFalse()
                    Step.JOB_PREFERENCES -> true
                    Step.COMPLETED -> true
                }
            }.collectLatest { isButtonEnabled ->
                updateState { copy(buttonData = buttonData.copy(enabled = isButtonEnabled)) }
            }
        }
    }

    private data class PersonalInformation(val fullName: String, val aboutMe: String) {
        fun isValid() = fullName.isNotBlank() && aboutMe.trim().length >= MIN_ABOUT_ME_LENGTH

        private companion object {
            const val MIN_ABOUT_ME_LENGTH = 100
        }
    }
}
