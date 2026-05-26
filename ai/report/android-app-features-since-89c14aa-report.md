# Android App Features Since `89c14aa`

## Scope

Base commit: `89c14aa84583ada4b18902dd0d7003deab54c1d5`

Current state reviewed: `HEAD=b37c603` plus current working tree changes.

This report keeps only the most important code fragments: feature contracts, network endpoints, ViewModel actions, navigation wiring, and regression tests. Pure strings/resources and broad UI markup are omitted unless they explain a feature action.

Status legend:

- **Committed**: included in `89c14aa..HEAD`.
- **Dirty**: current working-tree change not committed yet.

## 1. Navigation3 And Result Bus

Feature covered: central navigation setup, public routes, and result passing for cover letter templates.

Why chosen: this is the single registration point for all new route groups and the Navigation3 result bus.

Status: **Committed**

```kotlin
entryProvider = entryProvider {
    authEntry(controller = controller)
    employerEntry(controller = controller)
    publicEntry(controller = controller)
    seekerEntry(controller = controller)
    splashEntry(controller = controller)
},
entryDecorators = listOf(
    rememberSaveableStateHolderNavEntryDecorator(),
    rememberViewModelStoreNavEntryDecorator(),
    rememberResultEventBusNavEntryDecorator(),
),
```

## 2. Public Vacancy Browsing

Feature covered: view/search vacancies without login.

Why chosen: shows the core public discovery flow from debounced search to repository data and UI state.

Status: **Committed**

```kotlin
private fun loadVacancies(search: String? = null) {
    launch {
        val state = vacancyRepository.getPublicVacancies(search)
            .fold(
                onSuccess = { PublicVacanciesListState.Data(it.map { vacancy -> vacancy.toCardData() }) },
                onFailure = { PublicVacanciesListState.Empty },
            )
        updateState { copy(vacanciesListState = state) }
    }
}
```

## 3. Vacancy Repository Contract

Feature covered: public vacancies, applied vacancies, and favorites.

Why chosen: concise domain-level summary of the new vacancy capabilities.

Status: **Committed**, with `statuses` parameter currently **Dirty**.

```kotlin
interface VacancyRepository {
    val favoriteVacancyChanges: SharedFlow<FavoriteVacancyChange>

    suspend fun getPublicVacancies(query: String?): Result<List<Vacancy>>
    suspend fun getPublicVacancyById(id: Long): Result<Vacancy>
    suspend fun getAppliedVacancies(statuses: List<ApplicationStatus> = emptyList()): Result<List<Vacancy>>
    suspend fun getFavoriteVacancies(): Result<List<Vacancy>>
    suspend fun addFavoriteVacancy(id: Long): Result<Unit>
    suspend fun removeFavoriteVacancy(id: Long): Result<Unit>
}
```

## 4. Public/Favorite/Applied Network Endpoints

Feature covered: public browsing, favorite list, and applied vacancy filtering.

Why chosen: proves network integration and shows public client vs authenticated client split.

Status: public/favorite parts **Committed**; applied status filter **Dirty**.

```kotlin
override suspend fun getPublicVacancies(limit: Int, offset: Int, search: String?) =
    publicHttpClient.get(Vacancy(limit = limit, offset = offset, search = search))
        .networkResponse<VacancyListResponse>()

override suspend fun getFavoriteVacancies(limit: Int, offset: Int) =
    httpClient.get(FavoriteVacancy(limit = limit, offset = offset))
        .networkResponse<VacancyListResponse>()

override suspend fun getAppliedVacancies(
    limit: Int,
    offset: Int,
    statuses: List<ApplicationStatusRequest>
) = httpClient.get(
    Vacancy.Applied(
        limit = limit,
        offset = offset,
        application_status = statuses.takeIf { it.isNotEmpty() }
    )
).networkResponse<VacancyListResponse>()
```

## 5. Favorite Vacancy Toggle

Feature covered: add/remove favorite vacancy from seeker main list.

Why chosen: this is the actual user action, not only the API contract.

Status: **Committed**

```kotlin
private fun toggleFavorite(vacancyId: Long, isFavorite: Boolean) {
    launch {
        val result = if (isFavorite) {
            vacancyRepository.removeFavoriteVacancy(vacancyId)
        } else {
            vacancyRepository.addFavoriteVacancy(vacancyId)
        }
        result.onSuccess {
            updateVacancyFavoriteState(vacancyId = vacancyId, isFavorite = !isFavorite)
        }
    }
}
```

## 6. Favorite Vacancies List

Feature covered: dedicated favorite vacancies screen.

Why chosen: shows the full load state flow: loading, empty, data, and error.

Status: **Committed**, snackbar additions in current file are **Dirty**.

```kotlin
private fun loadVacancies() {
    launch {
        updateState { copy(contentState = FavoriteVacanciesContentState.Loading) }
        vacancyRepository.getFavoriteVacancies()
            .onSuccess { vacancies ->
                val contentState = if (vacancies.isEmpty()) {
                    FavoriteVacanciesContentState.Empty
                } else {
                    FavoriteVacanciesContentState.Data(vacancies.map { it.toCardData() })
                }
                updateState { copy(contentState = contentState) }
            }
    }
}
```

## 7. Applied Vacancies Status Filter

Feature covered: filtering applied vacancies by application status.

Why chosen: selected UI statuses directly drive the repository query.

Status: **Dirty**

```kotlin
private fun loadVacancies() {
    launch {
        updateState { copy(contentState = AppliedVacanciesContentState.Loading) }
        val statuses = mutableState.value.selectedStatuses.toList()
        vacancyRepository.getAppliedVacancies(statuses = statuses)
            .onSuccess { vacancies ->
                val contentState = if (vacancies.isEmpty()) {
                    AppliedVacanciesContentState.Empty
                } else {
                    AppliedVacanciesContentState.Data(vacancies.map { it.toCardData() })
                }
                updateState { copy(contentState = contentState) }
            }
    }
}
```

## 8. Applied Vacancy Opens Application Details

Feature covered: applied vacancies list navigates to application details.

Why chosen: key UX behavior changed from opening vacancy details to opening application details.

Status: **Dirty**

```kotlin
VacancyCard(
    modifier = Modifier.animateItem(),
    data = vacancy.vacancy,
    onClick = { onApplicationClick(vacancy.applicationId) }
)
```

## 9. Withdraw Application

Feature covered: seeker can withdraw an application.

Why chosen: combines endpoint and ViewModel execution path.

Status: endpoint **Committed**; ViewModel flow **Dirty**.

```kotlin
override suspend fun withdrawApplication(id: Long): NetworkResponse<Unit> {
    return httpClient
        .patch(Application.Withdraw(id = id))
        .networkResponse<Unit>()
}
```

```kotlin
private fun withdraw() {
    launch {
        updateState { copy(isWithdrawing = true, isWithdrawConfirmationVisible = false) }
        withdrawApplicationUseCase(applicationId)
            .onSuccess {
                _sideEffect.emit(ApplicationDetailsSideEffect.WithdrawSuccess)
                loadApplicationDetails()
            }
            .onFailure {
                showSnackBar(SnackBarData(message = ...))
            }
        updateState { copy(isWithdrawing = false) }
    }
}
```

## 10. Cover Letter Templates Contract

Feature covered: cover letter template CRUD.

Why chosen: concise repository contract for the whole feature.

Status: **Committed**

```kotlin
interface CoverLetterTemplateRepository {
    suspend fun createTemplate(message: String): Result<CoverLetterTemplate>
    suspend fun getTemplates(): Result<List<CoverLetterTemplate>>
    suspend fun getTemplate(id: Long): Result<CoverLetterTemplate>
    suspend fun updateTemplate(id: Long, message: String): Result<Unit>
    suspend fun deleteTemplate(id: Long): Result<Unit>
}
```

## 11. Cover Letter Template Save Logic

Feature covered: create and update template from the same editor flow.

Why chosen: this is the main business branch in the templates ViewModel.

Status: **Committed**, current file has additional **Dirty** tweaks.

```kotlin
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
            updateState { copy(selectedTemplate = null, isEditing = false, editingMessage = "") }
            loadTemplates()
        }
    }
}
```

## 12. Template Applied Back To Vacancy

Feature covered: selected cover letter template returns into the vacancy apply sheet.

Why chosen: shows Navigation3 result bus end-to-end.

Status: **Committed**

```kotlin
viewModel.sideEffect.collectAsEffect { effect ->
    when (effect) {
        is CoverLetterTemplatesSideEffect.ApplyTemplate -> {
            resultEventBus.sendResult(resultKey, effect.message)
            onBack()
        }
    }
}
```

```kotlin
ResultEffect<String>(coverLetterResultKey(vacancyId)) { coverLetter ->
    viewModel.sendAction(VacancyDetailsAction.ApplyCoverLetterTemplate(coverLetter))
}
```

## 13. Seeker Profile Edit Payload

Feature covered: editing seeker profile, including work experience.

Why chosen: exact backend request shape for profile editing.

Status: **Committed**

```kotlin
data class UpdateSeekerProfileRequest(
    @SerialName("speciality")
    val speciality: String,
    @SerialName("desired_salary")
    val desiredSalary: Int,
    @SerialName("experience_years")
    val experienceYears: Int,
    @SerialName("about_me")
    val aboutMe: String?,
    @SerialName("job_category")
    val jobCategory: JobCategoryCodeResponse,
    @SerialName("work_experience")
    val workExperience: List<UpdateWorkExperienceRequest>,
)
```

## 14. Save Edited Seeker Profile

Feature covered: ViewModel builds edited profile and persists it.

Why chosen: shows validation, domain object creation, repository update, and navigation back.

Status: **Committed**

```kotlin
private fun saveProfile() {
    val current = state.value
    val category = current.jobCategory ?: return
    val desiredSalary = current.desiredSalary.toIntOrNull() ?: return
    val experienceYears = current.experienceYears.toIntOrNull() ?: return
    if (current.speciality.isBlank()) return
    launch {
        updateState { copy(isSaving = true) }
        val updatedProfile = SeekerProfile(
            id = seekerProfileId,
            speciality = current.speciality,
            desiredSalary = desiredSalary,
            experienceYears = experienceYears,
            aboutMe = current.aboutMe.ifBlank { null },
            jobCategory = category,
            workExperience = current.workExperiences.map { ... },
        )
        seekerRepository.updateProfile(updatedProfile)
        _sideEffect.emit(EditSeekerProfileSideEffect.NavigateBack)
    }
}
```

## 15. Employer Role Edit

Feature covered: employer can update profile role.

Why chosen: smallest meaningful snippet for employer profile editing.

Status: **Committed**, current file has **Dirty** UI tweaks.

```kotlin
private fun saveRole() {
    val role = state.value.editingRole.trim()
    if (role.isBlank()) return
    launch {
        updateState { copy(isSavingRole = true) }
        val profile = employerRepository.updateProfileRole(role)
        updateState {
            copy(
                profile = profile,
                isRoleDialogVisible = false,
                isSavingRole = false
            )
        }
    }
}
```

## 16. Duplicate Vacancy

Feature covered: employer duplicates a vacancy by pre-filling create form from an existing vacancy.

Why chosen: clear feature evidence: source vacancy data becomes create vacancy state.

Status: **Committed**

```kotlin
private fun prefillFromSourceVacancy() {
    val id = sourceVacancyId ?: return
    launch {
        vacancyRepository.getVacancyById(id).onSuccess { vacancy ->
            updateState {
                copy(
                    title = vacancy.title,
                    description = vacancy.description,
                    salaryMin = vacancy.salaryMin.toString(),
                    salaryMax = vacancy.salaryMax.toString(),
                    experienceYears = (vacancy.minExperienceYears ?: 0).toString(),
                    selectedEmploymentType = vacancy.employmentType,
                    selectedCategory = vacancy.category,
                    prefillVersion = prefillVersion + 1,
                )
            }
        }
    }
}
```

## 17. Logout

Feature covered: logout clears local auth state and Ktor bearer token cache.

Why chosen: important session behavior after profile logout.

Status: **Committed**

```kotlin
override suspend fun logOut(): Result<Unit> {
    authTokenManager.clearTokens()
    httpClient.authProvider<BearerAuthProvider>()?.clearToken()
    return Result.success(Unit)
}
```

## 18. Navigation Regression Test

Feature covered: protected navigation history is cleared on auth reset/logout.

Why chosen: direct regression guard for the Navigation3 controller behavior.

Status: **Committed**

```kotlin
fun replaceAll_auth_clearsProtectedHistory() {
    val controller = createController()
    controller.replaceAll(Screens.Seeker.Main)
    controller.navigate(Screens.Seeker.Profile)

    controller.replaceAll(Screens.Auth)

    assertEquals(listOf(Screens.Auth), controller.stack.toList())
}
```

## Notes

- Dirty/uncommitted snippets should be removed if the report must cover only committed `HEAD`.
- Added tests also cover vacancy mapper counts, application repository mapping, and vacancy details eligibility.
- Not included: pure strings/resources, broad UI markup, docs, and generated/report files.
