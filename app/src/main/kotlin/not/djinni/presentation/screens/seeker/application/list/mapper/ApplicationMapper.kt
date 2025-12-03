@file:OptIn(ExperimentalTime::class)

package not.djinni.presentation.screens.seeker.application.list.mapper

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import not.djinni.R
import not.djinni.core.extension.toFormattedFullDate
import not.djinni.model.application.ApplicationDetails
import not.djinni.model.application.ApplicationStatus
import not.djinni.presentation.core.components.base.model.ApplicationCardData
import not.djinni.presentation.core.extension.toTextData
import not.djinni.presentation.theme.NotDjinniColor
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

internal fun ApplicationDetails.toCardData(): ApplicationCardData {
    val coverLetterPreview = coverLetter?.take(COVER_LETTER_PREVIEW_LENGTH)?.let { "$it..." }
    return ApplicationCardData(
        id = id,
        speciality = vacancy.title.toTextData(),
        experienceYears = vacancy.company.name.toTextData(),
        status = status.toDisplayStringRes().toTextData(),
        statusColor = status.toThemeColor(NotDjinniColor()),
        coverLetterPreview = coverLetterPreview?.toTextData(),
        appliedDate = formatDate(createdAt),
    )
}

@StringRes
fun ApplicationStatus.toDisplayStringRes(): Int = when (this) {
    ApplicationStatus.APPLIED -> R.string.application_status_applied
    ApplicationStatus.REVIEWING -> R.string.application_status_reviewing
    ApplicationStatus.INTERVIEW -> R.string.application_status_interview
    ApplicationStatus.TEST_TASK -> R.string.application_status_test_task
    ApplicationStatus.OFFER -> R.string.application_status_offer
    ApplicationStatus.HIRED -> R.string.application_status_hired
    ApplicationStatus.REJECTED -> R.string.application_status_rejected
    ApplicationStatus.WITHDRAWN -> R.string.application_status_withdrawn
}

fun ApplicationStatus.toThemeColor(colors: NotDjinniColor): Color = when (this) {
    ApplicationStatus.APPLIED -> colors.applicationStatusApplied
    ApplicationStatus.REVIEWING -> colors.applicationStatusReviewing
    ApplicationStatus.INTERVIEW -> colors.applicationStatusInterviewing
    ApplicationStatus.TEST_TASK -> colors.applicationStatusInterviewing
    ApplicationStatus.OFFER -> colors.applicationStatusOffered
    ApplicationStatus.HIRED -> colors.applicationStatusAccepted
    ApplicationStatus.REJECTED -> colors.applicationStatusRejected
    ApplicationStatus.WITHDRAWN -> colors.applicationStatusRejected
}

private fun formatDate(instant: Instant): not.djinni.presentation.core.components.base.model.TextData {
    return instant.toFormattedFullDate().toTextData()
}

private const val COVER_LETTER_PREVIEW_LENGTH = 100
