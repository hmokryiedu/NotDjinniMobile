package not.djinni.presentation.screens.seeker.main.model

import not.djinni.presentation.core.components.base.model.TextData

data class VacancyCardData(
    val id: Long,
    val title: TextData,
    val companyName: TextData,
    val salaryRange: TextData,
    val employmentType: TextData,
    val requiredExperience: TextData,
)