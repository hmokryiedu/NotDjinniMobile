package not.djinni.presentation.screens.splash

sealed interface SplashSideEffect {
    data object NavigateToAuth : SplashSideEffect
    data object NavigateToChooseRole : SplashSideEffect
}