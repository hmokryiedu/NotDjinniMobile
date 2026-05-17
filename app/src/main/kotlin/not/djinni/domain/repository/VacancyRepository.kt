package not.djinni.domain.repository

import not.djinni.model.seeker.vacancy.EmploymentType
import not.djinni.model.seeker.vacancy.JobCategoryCode
import not.djinni.model.seeker.vacancy.Vacancy

interface VacancyRepository {
    suspend fun getAllVacancies(query: String?): Result<List<Vacancy>>
    suspend fun getVacancyById(id: Long): Result<Vacancy>
    suspend fun getPublicVacancies(query: String?): Result<List<Vacancy>>
    suspend fun getPublicVacancyById(id: Long): Result<Vacancy>
    suspend fun getAppliedVacancies(): Result<List<Vacancy>>
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
