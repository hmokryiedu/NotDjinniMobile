package not.djinni.presentation.screens.seeker.application.list

import kotlinx.coroutines.flow.asSharedFlow
import not.djinni.R
import not.djinni.core.extension.mutableSideEffect
import not.djinni.domain.usecase.application.GetSeekerApplicationsUseCase
import not.djinni.presentation.core.StateViewModel
import not.djinni.presentation.core.components.base.model.SnackBarData
import not.djinni.presentation.core.extension.toTextData
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ViewApplicationsViewModel(
    private val getSeekerApplicationsUseCase: GetSeekerApplicationsUseCase,
) : StateViewModel<ViewApplicationsState>(ViewApplicationsState()) {

    private val _sideEffect = mutableSideEffect<ViewApplicationsSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        loadApplications()
    }

    fun sendAction(action: ViewApplicationsAction) {
        when (action) {
            ViewApplicationsAction.LoadApplications -> loadApplications()
            is ViewApplicationsAction.OpenApplicationDetails -> openApplicationDetails(action.id)
        }
    }

    private fun loadApplications() {
        launch(loadingEnabled = true) {
            getSeekerApplicationsUseCase()
                .onSuccess { applications ->
                    updateState {
                        copy(contentState = ContentState.Data(applications))
                    }
                }
                .onFailure { error ->
                    updateState { copy(contentState = ContentState.Error(R.string.no_applications_found.toTextData())) }
                    error.message?.let { showSnackBar(SnackBarData(it.toTextData())) }
                }
        }
    }

    private fun openApplicationDetails(id: Long) {
        launch {
            _sideEffect.emit(ViewApplicationsSideEffect.NavigateToDetails(id))
        }
    }
}
