package not.djinni.presentation.screens.onboarding

import not.djinni.presentation.core.StateViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class OnboardingViewModel : StateViewModel<OnboardingState>(OnboardingState())
