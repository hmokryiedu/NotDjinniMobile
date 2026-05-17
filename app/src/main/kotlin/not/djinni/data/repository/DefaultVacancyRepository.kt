package not.djinni.data.repository

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import not.djinni.domain.repository.FavoriteVacancyChange
import not.djinni.domain.repository.VacancyRepository
import not.djinni.model.seeker.vacancy.EmploymentType
import not.djinni.model.seeker.vacancy.JobCategoryCode
import not.djinni.model.seeker.vacancy.Vacancy
import not.djinni.model.seeker.vacancy.toDomain
import not.djinni.model.seeker.vacancy.toResponse
import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.vacancy.VacancyDataSource
import not.djinni.network.vacancy.request.CreateVacancyRequest
import not.djinni.network.vacancy.response.VacancyDetailsResponse
import not.djinni.network.vacancy.response.VacancyStatusCodeResponse
import org.koin.core.annotation.Single

@Single(binds = [VacancyRepository::class])
class DefaultVacancyRepository(
    private val dataSource: VacancyDataSource,
) : VacancyRepository {

    private val _favoriteVacancyChanges = MutableSharedFlow<FavoriteVacancyChange>(extraBufferCapacity = 1)
    override val favoriteVacancyChanges = _favoriteVacancyChanges.asSharedFlow()

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

    override suspend fun getPublicVacancies(query: String?) = runCatching {
        when (val response = dataSource.getPublicVacancies(search = query)) {
            is NetworkResponse.Success -> response.data.vacancies.map(VacancyDetailsResponse::toDomain)
            is NetworkResponse.Error -> throw Exception("Vacancies are empty or error occurred")
        }
    }

    override suspend fun getPublicVacancyById(id: Long) = runCatching {
        when (val response = dataSource.getPublicVacancyById(id)) {
            is NetworkResponse.Success -> response.data.toDomain()
            is NetworkResponse.Error -> throw Exception("Failed to load vacancy details")
        }
    }

    override suspend fun getAppliedVacancies(): Result<List<Vacancy>> = runCatching {
        when (val response = dataSource.getAppliedVacancies()) {
            is NetworkResponse.Success -> response.data.vacancies.map(VacancyDetailsResponse::toDomain)
            is NetworkResponse.Error -> throw Exception(response.error)
        }
    }

    override suspend fun getFavoriteVacancies(): Result<List<Vacancy>> = runCatching {
        when (val response = dataSource.getFavoriteVacancies()) {
            is NetworkResponse.Success -> response.data.vacancies.map(VacancyDetailsResponse::toDomain)
            is NetworkResponse.Error -> throw Exception(response.error)
        }
    }

    override suspend fun addFavoriteVacancy(id: Long): Result<Unit> = runCatching {
        when (val response = dataSource.addFavoriteVacancy(id)) {
            is NetworkResponse.Success -> {
                _favoriteVacancyChanges.emit(FavoriteVacancyChange(vacancyId = id, isFavorite = true))
                Unit
            }
            is NetworkResponse.Error -> throw Exception(response.error)
        }
    }

    override suspend fun removeFavoriteVacancy(id: Long): Result<Unit> = runCatching {
        when (val response = dataSource.removeFavoriteVacancy(id)) {
            is NetworkResponse.Success -> {
                _favoriteVacancyChanges.emit(FavoriteVacancyChange(vacancyId = id, isFavorite = false))
                Unit
            }
            is NetworkResponse.Error -> throw Exception(response.error)
        }
    }

    override suspend fun createVacancy(
        title: String,
        description: String,
        salaryMin: Int,
        salaryMax: Int,
        experienceYears: Int,
        employmentType: EmploymentType,
        category: JobCategoryCode
    ): Result<Vacancy> = runCatching {
        val request = CreateVacancyRequest(
            title = title,
            description = description,
            salaryMin = salaryMin,
            salaryMax = salaryMax,
            minExperienceYears = experienceYears,
            employmentType = employmentType.toResponse(),
            category = category.toResponse(),
            status = VacancyStatusCodeResponse.ACTIVE
        )
        when (val response = dataSource.createVacancy(request)) {
            is NetworkResponse.Success -> response.data.toDomain()
            is NetworkResponse.Error -> throw Exception(response.error)
        }
    }
}
