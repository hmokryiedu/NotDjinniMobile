@file:OptIn(ExperimentalTime::class)

package not.djinni.presentation.screens.seeker.vacancy.details

import kotlinx.coroutines.flow.asSharedFlow
import not.djinni.R
import not.djinni.core.extension.mutableSideEffect
import not.djinni.core.extension.toFormattedFullDate
import not.djinni.domain.repository.SeekerRepository
import not.djinni.domain.repository.VacancyRepository
import not.djinni.model.seeker.SeekerProfile
import not.djinni.model.seeker.vacancy.Vacancy
import not.djinni.presentation.core.StateViewModel
import not.djinni.presentation.core.components.base.model.TextData
import not.djinni.presentation.core.extension.toDisplayName
import not.djinni.presentation.core.extension.toTextData
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam
import kotlin.time.ExperimentalTime

@KoinViewModel
internal class VacancyDetailsViewModel(
    @InjectedParam private val vacancyId: Long,
    private val vacancyRepository: VacancyRepository,
    private val seekerRepository: SeekerRepository,
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
            VacancyDetailsAction.NavigateBack -> _sideEffect.tryEmit(VacancyDetailsSideEffect.NavigateBack)
        }
    }

    private fun loadVacancyDetails() {
        launch {
            updateState { copy(contentState = VacancyDetailsContentState.Loading) }
            vacancyRepository.getVacancyById(vacancyId)
                .onSuccess { vacancy ->
                    val profile = seekerRepository.getProfile()
                    val displayData = vacancy.toDisplayData()
                    val eligibility = profile?.let { calculateEligibility(vacancy, it) }
                    val data = VacancyDetailsContentState.Data(
                        vacancy = displayData,
                        eligibility = eligibility
                    )
                    updateState { copy(contentState = data) }
                }
                .onFailure {
                    val errorState =
                        VacancyDetailsContentState.Error(R.string.vacancy_details_error.toTextData())
                    updateState { copy(contentState = errorState) }
                }
        }
    }

    private fun handleApply() {
        val state = mutableState.value.contentState
        if (state is VacancyDetailsContentState.Data && state.eligibility?.canApply == true) {
            _sideEffect.tryEmit(VacancyDetailsSideEffect.NavigateToApply)
        }
    }

    private fun calculateEligibility(vacancy: Vacancy, profile: SeekerProfile): EligibilityState {
        val experienceMatch = vacancy.minExperienceYears == null ||
                profile.experienceYears >= vacancy.minExperienceYears
        val salaryMatch = vacancy.salaryMax >= profile.desiredSalary
        val salaryHint = if (!salaryMatch) {
            TextData.Text("Salary is below your expectations (\$${profile.desiredSalary})")
        } else null

        return EligibilityState(
            canApply = experienceMatch,
            experienceMatch = experienceMatch,
            salaryMatch = salaryMatch,
            salaryHint = salaryHint
        )
    }

    private fun Vacancy.toDisplayData(): VacancyDisplayData {
        return VacancyDisplayData(
            id = id,
            title = title.toTextData(),
            companyName = company.name.toTextData(),
            companyDescription = company.description.toTextData(),
            description = description.toTextData(),
            salaryRange = "$$salaryMin - $$salaryMax".toTextData(),
            employmentType = employmentType.toDisplayName(),
            requiredExperience = formatExperience(minExperienceYears),
            category = category?.toDisplayName(),
            postedDate = createdAt.toFormattedFullDate().toTextData()
        )
    }

    private fun formatExperience(years: Int?): TextData {
        return if (years == null || years == 0) {
            R.string.vacancy_no_experience_required.toTextData()
        } else {
            TextData.Text("$years+ years experience")
        }
    }
}
