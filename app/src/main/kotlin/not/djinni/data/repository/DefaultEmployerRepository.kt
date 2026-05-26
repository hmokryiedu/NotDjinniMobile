package not.djinni.data.repository

import not.djinni.data.mapper.toDomain
import not.djinni.domain.repository.EmployerRepository
import not.djinni.model.employer.EmployerProfile
import not.djinni.model.seeker.vacancy.Vacancy
import not.djinni.model.seeker.vacancy.toDomain
import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.employer.EmployerDataSource
import not.djinni.network.employer.request.CreateEmployerProfileRequest
import not.djinni.network.employer.request.UpdateEmployerProfileRequest
import not.djinni.network.vacancy.response.VacancyDetailsResponse
import org.koin.core.annotation.Single

@Single(binds = [EmployerRepository::class])
class DefaultEmployerRepository(
    private val remoteDataSource: EmployerDataSource,
) : EmployerRepository {

    override suspend fun getProfile(): EmployerProfile? {
        return when (val response = remoteDataSource.getProfile()) {
            is NetworkResponse.Success -> response.data.toDomain()
            is NetworkResponse.Error -> null
        }
    }

    override suspend fun createProfile(companyId: Long, role: String): EmployerProfile {
        val request = CreateEmployerProfileRequest(companyId = companyId, role = role)
        return when (val response = remoteDataSource.createProfile(request)) {
            is NetworkResponse.Success -> response.data.toDomain()
            is NetworkResponse.Error -> throw Exception("Failed to create profile")
        }
    }

    override suspend fun updateProfileRole(role: String): EmployerProfile {
        return when (val response = remoteDataSource.updateProfileRole(UpdateEmployerProfileRequest(role = role))) {
            is NetworkResponse.Success -> response.data.toDomain()
            is NetworkResponse.Error -> throw Exception("Failed to update profile")
        }
    }

    override suspend fun getEmployerVacancies(search: String?): Result<List<Vacancy>> = runCatching {
        when (val response = remoteDataSource.getVacancies(search = search)) {
            is NetworkResponse.Success -> response.data.vacancies.map(VacancyDetailsResponse::toDomain)
            is NetworkResponse.Error -> throw Exception("Failed to load vacancies")
        }
    }
}
