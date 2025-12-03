package not.djinni.presentation.screens.splash

import kotlinx.coroutines.flow.asSharedFlow
import not.djinni.core.extension.mutableSideEffect
import not.djinni.domain.repository.TokenRepository
import not.djinni.presentation.core.BaseViewModel
import not.djinni.presentation.screens.splash.SplashSideEffect.NavigateToAuth
import not.djinni.presentation.screens.splash.SplashSideEffect.NavigateToChooseRole
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SplashViewModel(
    private val tokenRepository: TokenRepository,
) : BaseViewModel() {

    private val _sideEffect = mutableSideEffect<SplashSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        initialize()
    }

    private fun initialize() {
        launch {
            val isValid = runCatching { tokenRepository.validate() }.getOrDefault(false)
            _sideEffect.tryEmit(if (isValid) NavigateToChooseRole else NavigateToAuth)
        }
    }
}