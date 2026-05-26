package not.djinni.domain.repository

import kotlinx.coroutines.flow.SharedFlow
import not.djinni.model.application.ApplicationStatus
import not.djinni.model.seeker.vacancy.EmploymentType
import not.djinni.model.seeker.vacancy.JobCategoryCode
import not.djinni.model.seeker.vacancy.Vacancy

data class FavoriteVacancyChange(
    val vacancyId: Long,
    val isFavorite: Boolean,
)

interface VacancyRepository {
    val favoriteVacancyChanges: SharedFlow<FavoriteVacancyChange>

    suspend fun getAllVacancies(query: String?): Result<List<Vacancy>>
    suspend fun getVacancyById(id: Long): Result<Vacancy>
    suspend fun getPublicVacancies(query: String?): Result<List<Vacancy>>
    suspend fun getPublicVacancyById(id: Long): Result<Vacancy>
    suspend fun getAppliedVacancies(statuses: List<ApplicationStatus> = emptyList()): Result<List<Vacancy>>
    suspend fun getFavoriteVacancies(): Result<List<Vacancy>>
    suspend fun addFavoriteVacancy(id: Long): Result<Unit>
    suspend fun removeFavoriteVacancy(id: Long): Result<Unit>
    suspend fun createVacancy(
        title: String,
        description: String,
        salaryMin: Int,
        salaryMax: Int,
        experienceYears: Int,
        employmentType: EmploymentType,
        category: JobCategoryCode
    ): Result<Vacancy>
}
