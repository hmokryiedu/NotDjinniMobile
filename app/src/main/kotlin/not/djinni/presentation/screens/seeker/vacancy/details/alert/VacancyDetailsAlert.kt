package not.djinni.presentation.screens.seeker.vacancy.details.alert

import not.djinni.presentation.core.components.base.model.TextData

sealed class VacancyDetailsAlert {
    data class Applying(val vacancyTitle: TextData) : VacancyDetailsAlert()
}