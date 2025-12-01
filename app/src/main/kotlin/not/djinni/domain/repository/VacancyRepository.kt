package not.djinni.domain.repository

import not.djinni.model.seeker.vacancy.Vacancy

interface VacancyRepository {
    suspend fun getAllVacancies(query: String?): Result<List<Vacancy>>
}