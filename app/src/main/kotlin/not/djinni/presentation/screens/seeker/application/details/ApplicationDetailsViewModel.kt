package not.djinni.presentation.screens.seeker.application.details

import kotlinx.coroutines.flow.asSharedFlow
import not.djinni.core.extension.mutableSideEffect
import not.djinni.domain.usecase.application.GetApplicationDetailsUseCase
import not.djinni.domain.usecase.application.WithdrawApplicationUseCase
import not.djinni.presentation.core.StateViewModel
import not.djinni.presentation.core.components.base.model.TextData
import not.djinni.presentation.core.extension.toTextData
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam

@KoinViewModel
internal class ApplicationDetailsViewModel(
    @InjectedParam private val applicationId: Long,
    private val getApplicationDetailsUseCase: GetApplicationDetailsUseCase,
    private val withdrawApplicationUseCase: WithdrawApplicationUseCase,
) : StateViewModel<ApplicationDetailsState>(ApplicationDetailsState()) {

    private val _sideEffect = mutableSideEffect<ApplicationDetailsSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        loadApplicationDetails()
    }

    fun sendAction(action: ApplicationDetailsAction) {
        when (action) {
            ApplicationDetailsAction.NavigateBack -> navigateBack()
            ApplicationDetailsAction.Withdraw -> withdraw()
        }
    }

    private fun loadApplicationDetails() {
        launch(loadingEnabled = true) {
            getApplicationDetailsUseCase(applicationId)
                .onSuccess { application ->
                    updateState { copy(contentState = ContentState.Data(application)) }
                }
                .onFailure { error ->
                    updateState {
                        copy(contentState = ContentState.Error(error.message?.toTextData() ?: TextData.Empty))
                    }
                }
        }
    }

    private fun navigateBack() {
        launch {
            _sideEffect.emit(ApplicationDetailsSideEffect.NavigateBack)
        }
    }

    private fun withdraw() {
        launch {
            updateState { copy(isWithdrawing = true) }
            withdrawApplicationUseCase(applicationId)
                .onSuccess {
                    _sideEffect.emit(ApplicationDetailsSideEffect.WithdrawSuccess)
                    loadApplicationDetails()
                }
            updateState { copy(isWithdrawing = false) }
        }
    }
}
