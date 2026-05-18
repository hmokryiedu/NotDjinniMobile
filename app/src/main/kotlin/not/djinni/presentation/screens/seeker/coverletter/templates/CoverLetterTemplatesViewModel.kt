package not.djinni.presentation.screens.seeker.coverletter.templates

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asSharedFlow
import not.djinni.R
import not.djinni.core.extension.mutableSideEffect
import not.djinni.domain.usecase.coverletter.CreateCoverLetterTemplateUseCase
import not.djinni.domain.usecase.coverletter.DeleteCoverLetterTemplateUseCase
import not.djinni.domain.usecase.coverletter.GetCoverLetterTemplateUseCase
import not.djinni.domain.usecase.coverletter.GetCoverLetterTemplatesUseCase
import not.djinni.domain.usecase.coverletter.UpdateCoverLetterTemplateUseCase
import not.djinni.presentation.core.StateViewModel
import not.djinni.utils.string.StringProvider
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class CoverLetterTemplatesViewModel(
    private val stringProvider: StringProvider,
    private val getTemplatesUseCase: GetCoverLetterTemplatesUseCase,
    private val getTemplateUseCase: GetCoverLetterTemplateUseCase,
    private val createTemplateUseCase: CreateCoverLetterTemplateUseCase,
    private val updateTemplateUseCase: UpdateCoverLetterTemplateUseCase,
    private val deleteTemplateUseCase: DeleteCoverLetterTemplateUseCase,
) : StateViewModel<CoverLetterTemplatesState>(CoverLetterTemplatesState()) {

    private val _sideEffect = mutableSideEffect<CoverLetterTemplatesSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        loadTemplates()
    }

    fun sendAction(action: CoverLetterTemplatesAction) {
        when (action) {
            CoverLetterTemplatesAction.Load -> loadTemplates()
            CoverLetterTemplatesAction.NavigateBack -> _sideEffect.tryEmit(CoverLetterTemplatesSideEffect.NavigateBack)
            CoverLetterTemplatesAction.CreateTemplate -> {
                updateState {
                    copy(
                        selectedTemplate = null,
                        isEditing = true,
                        editingMessage = ""
                    )
                }
            }
            is CoverLetterTemplatesAction.OpenTemplate -> openTemplate(action.id)
            CoverLetterTemplatesAction.StartEdit -> {
                val template = mutableState.value.selectedTemplate ?: return
                updateState { copy(isEditing = true, editingMessage = template.message) }
            }
            is CoverLetterTemplatesAction.UpdateEditingMessage -> {
                updateState { copy(editingMessage = action.message) }
            }
            CoverLetterTemplatesAction.Save -> saveTemplate()
            CoverLetterTemplatesAction.Delete -> deleteTemplate()
            CoverLetterTemplatesAction.Apply -> applyTemplate()
            CoverLetterTemplatesAction.DismissDialog -> {
                updateState {
                    copy(
                        selectedTemplate = null,
                        isEditing = false,
                        editingMessage = ""
                    )
                }
            }
        }
    }

    private fun loadTemplates() {
        launch {
            updateState { copy(contentState = CoverLetterTemplatesContentState.Loading) }
            getTemplatesUseCase()
                .onSuccess { templates ->
                    val display = templates.map { CoverLetterTemplateDisplayData(it.id, it.message) }
                    updateState {
                        copy(
                            contentState = if (display.isEmpty()) {
                                CoverLetterTemplatesContentState.Empty
                            } else {
                                CoverLetterTemplatesContentState.Data(display)
                            }
                        )
                    }
                }
                .onFailure {
                    updateState {
                        copy(
                            contentState = CoverLetterTemplatesContentState.Error(
                                stringProvider.getString(R.string.cover_letter_templates_load_error)
                            )
                        )
                    }
                }
        }
    }

    private fun openTemplate(id: Long) {
        launch {
            getTemplateUseCase(id)
                .onSuccess { template ->
                    updateState {
                        copy(
                            selectedTemplate = CoverLetterTemplateDisplayData(template.id, template.message),
                            isEditing = false,
                            editingMessage = template.message
                        )
                    }
                }
        }
    }

    private fun saveTemplate() {
        val message = mutableState.value.editingMessage.trim()
        if (message.length < MIN_TEMPLATE_LENGTH) return
        launch {
            val selected = mutableState.value.selectedTemplate
            val result = if (selected == null) {
                createTemplateUseCase(message).map { Unit }
            } else {
                updateTemplateUseCase(UpdateCoverLetterTemplateUseCase.Params(id = selected.id, message = message))
            }
            result.onSuccess {
                updateState {
                    copy(
                        selectedTemplate = null,
                        isEditing = false,
                        editingMessage = ""
                    )
                }
                loadTemplates()
            }
        }
    }

    private fun deleteTemplate() {
        val template = mutableState.value.selectedTemplate ?: return
        launch {
            deleteTemplateUseCase(template.id)
                .onSuccess {
                    updateState {
                        copy(
                            selectedTemplate = null,
                            isEditing = false,
                            editingMessage = ""
                        )
                    }
                    loadTemplates()
                }
        }
    }

    private fun applyTemplate() {
        val template = mutableState.value.selectedTemplate ?: return
        launch {
            updateState {
                copy(
                    selectedTemplate = null,
                    isEditing = false,
                    editingMessage = "",
                )
            }
            delay(APPLY_TEMPLATE_NAVIGATION_DELAY_MS)
            _sideEffect.tryEmit(CoverLetterTemplatesSideEffect.ApplyTemplate(template.message))
        }
    }

    companion object {
        const val MIN_TEMPLATE_LENGTH = 10
        private const val APPLY_TEMPLATE_NAVIGATION_DELAY_MS = 120L
    }
}
