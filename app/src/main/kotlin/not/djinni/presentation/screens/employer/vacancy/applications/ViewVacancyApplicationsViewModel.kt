@file:OptIn(ExperimentalTime::class)

package not.djinni.presentation.screens.employer.vacancy.applications

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.asSharedFlow
import not.djinni.R
import not.djinni.core.extension.mutableSideEffect
import not.djinni.core.extension.toFormattedFullDate
import not.djinni.domain.repository.ApplicationRepository
import not.djinni.model.application.ApplicationDetails
import not.djinni.model.application.ApplicationStatus
import not.djinni.presentation.core.StateViewModel
import not.djinni.presentation.core.components.base.model.ApplicationCardData
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.screens.employer.vacancy.applications.ViewVacancyApplicationsSideEffect.NavigateToApplicationDetails
import not.djinni.utils.string.StringProvider
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam
import kotlin.time.ExperimentalTime

@KoinViewModel
internal class ViewVacancyApplicationsViewModel(
    @InjectedParam private val vacancyId: Long,
    private val applicationRepository: ApplicationRepository,
    private val stringProvider: StringProvider,
) : StateViewModel<ViewVacancyApplicationsState>(ViewVacancyApplicationsState()) {

    private val _sideEffect = mutableSideEffect<ViewVacancyApplicationsSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun sendAction(action: ViewVacancyApplicationsAction) {
        when (action) {
            ViewVacancyApplicationsAction.NavigateBack -> {
                _sideEffect.tryEmit(ViewVacancyApplicationsSideEffect.NavigateBack)
            }
            is ViewVacancyApplicationsAction.OpenApplicationDetails -> {
                _sideEffect.tryEmit(
                    NavigateToApplicationDetails(action.applicationId)
                )
            }

            ViewVacancyApplicationsAction.LoadApplications -> loadApplications()
        }
    }

    private fun loadApplications() {
        launch {
            updateState { copy(contentState = ApplicationsContentState.Loading) }
            applicationRepository.getApplicationsByVacancy(vacancyId).fold(
                onSuccess = { applications ->
                    val contentState = if (applications.isEmpty()) {
                        ApplicationsContentState.Empty
                    } else {
                        ApplicationsContentState.Data(applications.map { it.toCardData() })
                    }
                    updateState { copy(contentState = contentState) }
                },
                onFailure = {
                    updateState {
                        copy(contentState = ApplicationsContentState.Error(message = R.string.vacancy_applications_error.toTextData()))
                    }
                }
            )
        }
    }

    private fun ApplicationDetails.toCardData(): ApplicationCardData {
        val experienceText = stringProvider.getString(
            R.string.vacancy_years_experience,
            jobSeeker.experienceYears
        )
        return ApplicationCardData(
            id = id,
            speciality = jobSeeker.speciality.toTextData(),
            experienceYears = experienceText.toTextData(),
            status = status.toDisplayText(),
            statusColor = status.toColor(),
            coverLetterPreview = coverLetter?.take(COVER_LETTER_PREVIEW_LENGTH)?.plus("...")
                ?.toTextData(),
            appliedDate = stringProvider.getString(
                R.string.application_applied_on,
                createdAt.toFormattedFullDate()
            ).toTextData()
        )
    }

    private fun ApplicationStatus.toDisplayText() = when (this) {
        ApplicationStatus.APPLIED -> R.string.application_status_applied
        ApplicationStatus.REVIEWING -> R.string.application_status_reviewing
        ApplicationStatus.INTERVIEW -> R.string.application_status_interview
        ApplicationStatus.TEST_TASK -> R.string.application_status_test_task
        ApplicationStatus.OFFER -> R.string.application_status_offer
        ApplicationStatus.HIRED -> R.string.application_status_hired
        ApplicationStatus.REJECTED -> R.string.application_status_rejected
        ApplicationStatus.WITHDRAWN -> R.string.application_status_withdrawn
    }.toTextData()

    private fun ApplicationStatus.toColor() = when (this) {
        ApplicationStatus.APPLIED,
        ApplicationStatus.REVIEWING,
        ApplicationStatus.INTERVIEW,
        ApplicationStatus.TEST_TASK -> StatusColors.primary

        ApplicationStatus.OFFER,
        ApplicationStatus.HIRED -> StatusColors.success

        ApplicationStatus.REJECTED -> StatusColors.error
        ApplicationStatus.WITHDRAWN -> StatusColors.withdrawn
    }

    private object StatusColors {
        val primary = Color.White
        val success = Color(0xFF4CAF50)
        val error = Color(0xFFF54927)
        val withdrawn = Color.White.copy(alpha = 0.5f)
    }

    private companion object {
        const val COVER_LETTER_PREVIEW_LENGTH = 100
    }
}
