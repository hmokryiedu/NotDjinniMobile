package not.djinni.presentation.core.extension

import not.djinni.R
import not.djinni.model.seeker.vacancy.VacancyStatusCode
import not.djinni.presentation.core.components.base.model.TextData

fun VacancyStatusCode.toDisplayName(): TextData = when (this) {
    VacancyStatusCode.DRAFT -> R.string.vacancy_status_draft.toTextData()
    VacancyStatusCode.ACTIVE -> R.string.vacancy_status_active.toTextData()
    VacancyStatusCode.PAUSED -> R.string.vacancy_status_paused.toTextData()
    VacancyStatusCode.CLOSED -> R.string.vacancy_status_closed.toTextData()
    VacancyStatusCode.EXPIRED -> R.string.vacancy_status_expired.toTextData()
}
