package not.djinni.data.repository

import not.djinni.domain.repository.VacancyRepository
import not.djinni.model.seeker.vacancy.toDomain
import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.vacancy.VacancyDataSource
import not.djinni.network.vacancy.response.VacancyDetailsResponse
import org.koin.core.annotation.Single

@Single(binds = [VacancyRepository::class])
class DefaultVacancyRepository(
    private val dataSource: VacancyDataSource,
) : VacancyRepository {

    override suspend fun getAllVacancies(query: String?) = runCatching {
        when (val response = dataSource.getAllVacancies(search = query)) {
            is NetworkResponse.Success -> response.data.vacancies.map(VacancyDetailsResponse::toDomain)
            is NetworkResponse.Error -> throw Exception("Vacancies are empty or error occurred")
        }
    }

    override suspend fun getVacancyById(id: Long) = runCatching {
        when (val response = dataSource.getVacancyById(id)) {
            is NetworkResponse.Success -> response.data.toDomain()
            is NetworkResponse.Error -> throw Exception("Failed to load vacancy details")
        }
    }
}