package not.djinni.presentation.core.components.base.model

data class VacancyCardData(
    val id: Long,
    val title: TextData,
    val companyName: TextData,
    val salaryRange: TextData,
    val employmentType: TextData,
    val requiredExperience: TextData,
    val isFavorite: Boolean = false,
)
