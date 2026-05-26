@file:OptIn(ExperimentalTime::class)

package not.djinni.presentation.screens.employer.vacancy.details

import kotlinx.coroutines.flow.asSharedFlow
import not.djinni.R
import not.djinni.core.extension.mutableSideEffect
import not.djinni.core.extension.toFormattedFullDate
import not.djinni.domain.repository.VacancyRepository
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
) : StateViewModel<VacancyDetailsState>(VacancyDetailsState()) {

    private val _sideEffect = mutableSideEffect<VacancyDetailsSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        loadVacancyDetails()
    }

    fun sendAction(action: VacancyDetailsAction) {
        when (action) {
            VacancyDetailsAction.Retry -> loadVacancyDetails()
            VacancyDetailsAction.NavigateBack -> _sideEffect.tryEmit(VacancyDetailsSideEffect.NavigateBack)
            VacancyDetailsAction.Duplicate -> _sideEffect.tryEmit(VacancyDetailsSideEffect.NavigateToDuplicate(vacancyId))
            VacancyDetailsAction.ViewApplications -> _sideEffect.tryEmit(VacancyDetailsSideEffect.NavigateToApplications(vacancyId))
        }
    }

    private fun loadVacancyDetails() {
        launch {
            updateState { copy(contentState = VacancyDetailsContentState.Loading) }

            vacancyRepository.getVacancyById(vacancyId).fold(
                onSuccess = { vacancy ->
                    updateState {
                        copy(contentState = VacancyDetailsContentState.Data(vacancy.toDisplayData()))
                    }
                },
                onFailure = {
                    updateState {
                        copy(contentState = VacancyDetailsContentState.Error(message = R.string.vacancy_details_error.toTextData()))
                    }
                }
            )
        }
    }

    private fun Vacancy.toDisplayData(): VacancyDisplayData {
        return VacancyDisplayData(
            id = id,
            title = title.toTextData(),
            description = description.toTextData(),
            salaryRange = "\$$salaryMin - \$$salaryMax".toTextData(),
            employmentType = employmentType.toDisplayName(),
            requiredExperience = formatExperience(minExperienceYears),
            category = category?.toDisplayName(),
            status = status.toDisplayName(),
            postedDate = createdAt.toFormattedFullDate().toTextData(),
            updatedDate = updatedAt.toFormattedFullDate().toTextData(),
            viewsCount = viewsCount.toString().toTextData(),
            applicationsCount = applicationsCount.toString().toTextData()
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
