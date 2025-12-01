package not.djinni.presentation.screens.employer.profile.create

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import not.djinni.R
import not.djinni.core.extension.mutableSideEffect
import not.djinni.domain.repository.CompanyRepository
import not.djinni.domain.repository.EmployerRepository
import not.djinni.presentation.core.StateViewModel
import not.djinni.presentation.core.components.base.model.TextData
import not.djinni.presentation.screens.employer.profile.create.alert.CreateEmployerProfileAlert
import org.koin.android.annotation.KoinViewModel

@OptIn(FlowPreview::class)
@KoinViewModel
internal class CreateEmployerProfileViewModel(
    private val employerRepository: EmployerRepository,
    private val companyRepository: CompanyRepository,
) : StateViewModel<CreateEmployerProfileState>(CreateEmployerProfileState()) {

    private val _sideEffect = mutableSideEffect<CreateEmployerProfileSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private val messages = Channel<TextData>(capacity = Channel.UNLIMITED)
    private val searchQueryFlow = MutableStateFlow("")

    init {
        collectMessages()
        collectSearchQuery()
    }

    fun sendAction(action: CreateEmployerProfileAction) {
        when (action) {
            CreateEmployerProfileAction.ShowSelectCompanyAlert -> showSelectCompanyAlert()
            CreateEmployerProfileAction.HideAlert -> hideAlert()
            is CreateEmployerProfileAction.SearchCompanies -> searchCompanies(action.query)
            is CreateEmployerProfileAction.SelectCompany -> selectCompany(action)
            is CreateEmployerProfileAction.CreateProfile -> createProfile(action)
        }
    }

    private fun showSelectCompanyAlert() {
        updateState { copy(currentAlert = CreateEmployerProfileAlert.SELECT_COMPANY) }
        loadInitialCompanies()
    }

    private fun hideAlert() {
        updateState { copy(currentAlert = null) }
        searchQueryFlow.tryEmit("")
    }

    private fun loadInitialCompanies() {
        launch {
            updateState { copy(isSearching = true) }
            val results = companyRepository.searchCompanies("")
            updateState { copy(searchResults = results, isSearching = false) }
        }
    }

    private fun searchCompanies(query: String) {
        searchQueryFlow.tryEmit(query)
    }

    private fun collectSearchQuery() {
        launch {
            searchQueryFlow
                .debounce(SEARCH_DEBOUNCE_MS)
                .distinctUntilChanged()
                .collect { query ->
                    updateState { copy(isSearching = true) }
                    val results = companyRepository.searchCompanies(query)
                    updateState { copy(searchResults = results, isSearching = false) }
                }
        }
    }

    private fun selectCompany(action: CreateEmployerProfileAction.SelectCompany) {
        updateState { copy(selectedCompany = action.company) }
        hideAlert()
    }

    private fun createProfile(action: CreateEmployerProfileAction.CreateProfile) {
        launch {
            val errorResId = getValidationErrorResId(action.role)
            if (errorResId != null) {
                messages.trySend(TextData.Resource(errorResId))
                return@launch
            }
            val company = state.value.selectedCompany ?: return@launch
            employerRepository.createProfile(companyId = company.id, role = action.role)
            _sideEffect.emit(CreateEmployerProfileSideEffect.NavigateToHome)
        }
    }

    private fun getValidationErrorResId(role: String): Int? {
        return when {
            role.isBlank() -> R.string.employer_role_required
            state.value.selectedCompany == null -> R.string.employer_company_required
            else -> null
        }
    }

    private fun collectMessages() {
        launch {
            messages.consumeAsFlow().collect { message ->
                updateState { copy(message = message) }
                delay(MESSAGE_DISPLAY_DURATION_MS)
                updateState { copy(message = null) }
            }
        }
    }

    private companion object {
        const val MESSAGE_DISPLAY_DURATION_MS = 2000L
        const val SEARCH_DEBOUNCE_MS = 300L
    }
}
