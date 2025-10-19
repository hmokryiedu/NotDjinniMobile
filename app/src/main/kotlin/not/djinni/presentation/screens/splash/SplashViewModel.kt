package not.djinni.presentation.screens.splash

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asSharedFlow
import not.djinni.core.extension.mutableSideEffect
import not.djinni.domain.usecase.auth.CheckIsLoggedInUseCase
import not.djinni.presentation.core.BaseViewModel
import not.djinni.presentation.screens.splash.SplashSideEffect.*
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SplashViewModel(
    private val checkIsLoggedInUseCase: CheckIsLoggedInUseCase
) : BaseViewModel() {

    private val _sideEffect = mutableSideEffect<SplashSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        initialize()
    }

    private fun initialize() {
        launch {
            delay(2000L) // TODO: Remove fake delay later
            val effect = if (checkIsLoggedInUseCase()) NavigateToMain else NavigateToAuth
            _sideEffect.tryEmit(effect)
        }
    }
}