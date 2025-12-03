@file:OptIn(ExperimentalTime::class)

package not.djinni.presentation.screens.employer.application.details

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.asSharedFlow
import not.djinni.R
import not.djinni.core.extension.mutableSideEffect
import not.djinni.domain.usecase.application.GetApplicationDetailsUseCase
import not.djinni.domain.usecase.application.UpdateApplicationStatusUseCase
import not.djinni.model.application.ApplicationDetails
import not.djinni.model.application.ApplicationStatus
import not.djinni.presentation.core.StateViewModel
import not.djinni.presentation.core.components.base.model.SnackBarData
import not.djinni.presentation.core.extension.toTextData
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.InjectedParam
import kotlin.time.ExperimentalTime
import kotlin.time.toJavaInstant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@KoinViewModel
internal class ApplicationDetailsViewModel(
    @InjectedParam private val applicationId: Long,
    private val getApplicationDetailsUseCase: GetApplicationDetailsUseCase,
    private val updateApplicationStatusUseCase: UpdateApplicationStatusUseCase,
) : StateViewModel<ApplicationDetailsState>(ApplicationDetailsState()) {

    private val _sideEffect = mutableSideEffect<ApplicationDetailsSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        loadApplicationDetails()
    }

    fun sendAction(action: ApplicationDetailsAction) {
        when (action) {
            ApplicationDetailsAction.NavigateBack -> _sideEffect.tryEmit(
                ApplicationDetailsSideEffect.NavigateBack
            )
            ApplicationDetailsAction.Retry -> loadApplicationDetails()
            ApplicationDetailsAction.ViewVacancyDetails -> onViewVacancyDetails()
            ApplicationDetailsAction.OpenUpdateStatusSheet -> openUpdateStatusSheet()
            ApplicationDetailsAction.DismissAlert -> dismissAlert()
            is ApplicationDetailsAction.UpdateStatus -> updateStatus(action.newStatus)
        }
    }

    private fun loadApplicationDetails() {
        launch(loadingEnabled = true) {
            getApplicationDetailsUseCase(applicationId)
                .onSuccess { details ->
                    updateState {
                        copy(
                            contentState = ContentState.Data(details.toDisplayData())
                        )
                    }
                }
                .onFailure { error ->
                    updateState {
                        copy(
                            contentState = ContentState.Error(
                                message = (error.message ?: "Unknown error").toTextData()
                            )
                        )
                    }
                }
        }
    }

    private fun onViewVacancyDetails() {
        val currentState = state.value.contentState
        if (currentState is ContentState.Data) {
            _sideEffect.tryEmit(
                ApplicationDetailsSideEffect.NavigateToVacancyDetails(
                    currentState.details.vacancyId
                )
            )
        }
    }

    private fun openUpdateStatusSheet() {
        updateState { copy(openedAlert = ApplicationDetailsAlert.UPDATE_STATUS) }
    }

    private fun dismissAlert() {
        updateState { copy(openedAlert = null) }
    }

    private fun updateStatus(newStatus: ApplicationStatus) {
        launch {
            val currentState = state.value.contentState
            if (currentState is ContentState.Data) {
                updateState {
                    copy(
                        openedAlert = null,
                        contentState = ContentState.Data(
                            currentState.details.copy(
                                currentStatus = newStatus,
                                status = newStatus.toDisplayName(),
                                statusColor = newStatus.toColor()
                            )
                        )
                    )
                }

                updateApplicationStatusUseCase(
                    UpdateApplicationStatusUseCase.Params(
                        applicationId = applicationId,
                        status = newStatus
                    )
                )
                    .onSuccess {
                        showSnackBar(
                            SnackBarData(
                                message = R.string.application_status_updated_success.toTextData()
                            )
                        )
                    }
                    .onFailure { error ->
                        showSnackBar(
                            SnackBarData(
                                message = (error.message ?: "Failed to update status").toTextData()
                            )
                        )
                        loadApplicationDetails()
                    }
            }
        }
    }

    private fun ApplicationDetails.toDisplayData(): ApplicationDetailsDisplayData {
        return ApplicationDetailsDisplayData(
            applicationId = id,
            currentStatus = status,
            status = status.toDisplayName(),
            statusColor = status.toColor(),
            seekerName = jobSeeker.speciality.toTextData(),
            seekerSpeciality = jobSeeker.speciality.toTextData(),
            seekerExperience = "${jobSeeker.experienceYears} years of experience".toTextData(),
            vacancyTitle = vacancy.title.toTextData(),
            vacancySalary = "$${vacancy.salaryMin} - $${vacancy.salaryMax}".toTextData(),
            vacancyEmploymentType = vacancy.employmentType.name.replace('_', ' ')
                .lowercase()
                .replaceFirstChar { it.uppercase() }
                .toTextData(),
            coverLetter = coverLetter?.toTextData(),
            appliedDate = createdAt.toJavaInstant().formatDate(),
            updatedDate = updatedAt.toJavaInstant().formatDate(),
            vacancyId = vacancy.id
        )
    }

    private fun ApplicationStatus.toDisplayName() = when (this) {
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

    private fun java.time.Instant.formatDate(): not.djinni.presentation.core.components.base.model.TextData {
        val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy")
            .withZone(ZoneId.systemDefault())
        return formatter.format(this).toTextData()
    }

    private object StatusColors {
        val primary = Color.White
        val success = Color(0xFF4CAF50)
        val error = Color(0xFFF54927)
        val withdrawn = Color.White.copy(alpha = 0.5f)
    }
}
