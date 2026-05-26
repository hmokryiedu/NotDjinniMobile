@file:OptIn(ExperimentalTime::class)

package not.djinni.presentation.screens.seeker.vacancy.details

import kotlinx.coroutines.flow.asSharedFlow
import not.djinni.R
import not.djinni.core.extension.mutableSideEffect
import not.djinni.core.extension.toFormattedFullDate
import not.djinni.domain.repository.ApplicationRepository
import not.djinni.domain.repository.SeekerRepository
import not.djinni.domain.repository.VacancyRepository
import not.djinni.model.seeker.vacancy.Vacancy
import not.djinni.presentation.core.StateViewModel
import not.djinni.presentation.core.components.base.model.SnackBarData
import not.djinni.presentation.core.components.base.model.TextData
import not.djinni.presentation.core.extension.toDisplayName
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.screens.seeker.vacancy.details.alert.VacancyDetailsAlert
import not.djinni.utils.string.StringProvider
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam
import kotlin.time.ExperimentalTime

@KoinViewModel
internal class VacancyDetailsViewModel(
    @InjectedParam private val vacancyId: Long,
    private val stringProvider: StringProvider,
    private val seekerRepository: SeekerRepository,
    private val vacancyRepository: VacancyRepository,
    private val applicationRepository: ApplicationRepository,
) : StateViewModel<VacancyDetailsState>(VacancyDetailsState()) {

    private val _sideEffect = mutableSideEffect<VacancyDetailsSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        loadVacancyDetails()
    }

    fun sendAction(action: VacancyDetailsAction) {
        when (action) {
            VacancyDetailsAction.Load -> loadVacancyDetails()
            VacancyDetailsAction.Apply -> handleApply()
            VacancyDetailsAction.SeeApplication -> seeApplication()
            VacancyDetailsAction.NavigateBack -> _sideEffect.tryEmit(VacancyDetailsSideEffect.NavigateBack)
            VacancyDetailsAction.ToggleFavorite -> toggleFavorite()
            VacancyDetailsAction.HideApplyBottomSheet -> updateState { copy(currentAlert = null, selectedCoverLetterTemplate = null) }
            VacancyDetailsAction.OpenCoverLetterTemplates -> _sideEffect.tryEmit(VacancyDetailsSideEffect.NavigateToCoverLetterTemplates)
            is VacancyDetailsAction.ApplyCoverLetterTemplate -> {
                updateState { copy(selectedCoverLetterTemplate = action.coverLetter) }
            }
            is VacancyDetailsAction.SubmitApplication -> submitApplication(action.coverLetter)
            VacancyDetailsAction.ShowApplicationSuccessSnackBar -> {
                showSnackBar(
                    SnackBarData(message = R.string.apply_vacancy_success.toTextData())
                )
            }
        }
    }

    private fun loadVacancyDetails() {
        launch {
            updateState { copy(contentState = VacancyDetailsContentState.Loading) }
            vacancyRepository.getVacancyById(vacancyId)
                .onSuccess { vacancy ->
                    val profile = seekerRepository.getProfile()
                    val displayData = vacancy.toDisplayData()
                    val eligibility = profile?.let {
                        calculateEligibility(
                            vacancy = vacancy,
                            profile = it,
                            salaryHintProvider = { desiredSalary ->
                                stringProvider.getString(
                                    R.string.vacancy_salary_is_below_your_expectations,
                                    desiredSalary
                                )
                            },
                            missingCategoryWarningProvider = {
                                stringProvider.getString(R.string.vacancy_missing_category_warning)
                            },
                            categoryMismatchBlockerProvider = {
                                stringProvider.getString(R.string.vacancy_category_mismatch_blocker)
                            },
                            inactiveVacancyBlockerProvider = {
                                stringProvider.getString(R.string.vacancy_status_not_active_blocker)
                            },
                            insufficientExperienceBlockerProvider = { minYears ->
                                stringProvider.getString(R.string.eligibility_experience_mismatch_with_years, minYears)
                            },
                        )
                    }
                    val isApplied = applicationRepository.isAppliedToVacancy(vacancyId)
                    updateState {
                        copy(
                            contentState = VacancyDetailsContentState.Data(
                                vacancy = displayData,
                                eligibility = eligibility
                            ),
                            isApplied = isApplied.getOrDefault(false)
                        )
                    }
                }
                .onFailure {
                    val errorState = VacancyDetailsContentState.Error(
                        R.string.vacancy_details_error.toTextData()
                    )
                    updateState { copy(contentState = errorState) }
                }
        }
    }

    private fun handleApply() {
        val state = mutableState.value.apply { if (isApplied) return }
        val contentState = state.contentState
        if (contentState is VacancyDetailsContentState.Data && contentState.eligibility.isApplyAvailable()) {
            val alert = VacancyDetailsAlert.Applying
            updateState { copy(currentAlert = alert) }
        }
    }

    private fun seeApplication() {
        launch {
            applicationRepository.getMyApplicationByVacancy(vacancyId)
                .onSuccess { application ->
                    _sideEffect.tryEmit(
                        VacancyDetailsSideEffect.NavigateToApplicationDetails(application.id)
                    )
                }
                .onFailure {
                    showSnackBar(
                        SnackBarData(message = R.string.vacancy_details_error.toTextData())
                    )
                }
        }
    }

    private fun submitApplication(coverLetter: String?) {
        launch {
            applicationRepository
                .applyToVacancy(vacancyId = vacancyId, coverLetter = coverLetter)
                .onSuccess {
                    hideAlert()
                    loadVacancyDetails()
                    _sideEffect.tryEmit(VacancyDetailsSideEffect.ApplicationSuccess)
                }
                .onFailure {
                    hideAlert()
                    showSnackBar(
                        SnackBarData(
                            message = (it.message ?: stringProvider.getString(R.string.apply_vacancy_error)).toTextData()
                        )
                    )
                }
        }
    }

    private fun toggleFavorite() {
        val contentState = mutableState.value.contentState
        if (contentState !is VacancyDetailsContentState.Data) return
        launch {
            val vacancy = contentState.vacancy
            val result = if (vacancy.isFavorite) {
                vacancyRepository.removeFavoriteVacancy(vacancy.id)
            } else {
                vacancyRepository.addFavoriteVacancy(vacancy.id)
            }
            result.onSuccess {
                updateState {
                    val currentContentState = this.contentState
                    if (currentContentState !is VacancyDetailsContentState.Data) return@updateState this
                    copy(
                        contentState = currentContentState.copy(
                            vacancy = currentContentState.vacancy.copy(
                                isFavorite = !currentContentState.vacancy.isFavorite
                            )
                        )
                    )
                }
                showSnackBar(
                    SnackBarData(
                        message = if (vacancy.isFavorite) {
                            R.string.favorite_vacancy_removed_success.toTextData()
                        } else {
                            R.string.favorite_vacancy_added_success.toTextData()
                        }
                    )
                )
            }
            result.onFailure {
                showSnackBar(
                    SnackBarData(
                        message = (it.message ?: stringProvider.getString(R.string.favorite_vacancy_update_error)).toTextData()
                    )
                )
            }
        }
    }

    private fun hideAlert() {
        updateState { copy(currentAlert = null) }
    }

    private fun Vacancy.toDisplayData(): VacancyDisplayData {
        return VacancyDisplayData(
            id = id,
            title = title.toTextData(),
            companyName = company.name.toTextData(),
            companyDescription = company.description.toTextData(),
            description = description.toTextData(),
            viewsCount = viewsCount.toString().toTextData(),
            applicationsCount = applicationsCount.toString().toTextData(),
            salaryRange = stringProvider.getString(
                R.string.vacancy_salary_range,
                salaryMin,
                salaryMax
            ).toTextData(),
            employmentType = employmentType.toDisplayName(),
            requiredExperience = formatExperience(minExperienceYears),
            category = category?.toDisplayName(),
            postedDate = createdAt.toFormattedFullDate().toTextData(),
            isFavorite = isFavorite
        )
    }

    private fun formatExperience(years: Int?): TextData {
        return if (years == null || years == 0) {
            R.string.vacancy_no_experience_required.toTextData()
        } else {
            stringProvider.getString(R.string.vacancy_years_experience, years).toTextData()
        }
    }

}
