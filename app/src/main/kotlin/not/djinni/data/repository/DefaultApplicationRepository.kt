package not.djinni.data.repository

import not.djinni.data.mapper.toDomain
import not.djinni.data.mapper.toRequest
import not.djinni.domain.repository.ApplicationRepository
import not.djinni.model.application.ApplicationDetails
import not.djinni.model.application.ApplicationStatus
import not.djinni.network.application.ApplicationDataSource
import not.djinni.network.application.request.CreateApplicationRequest
import not.djinni.network.application.request.UpdateApplicationStatusRequest
import not.djinni.network.common.response.NetworkResponse
import org.koin.core.annotation.Single

@Single(binds = [ApplicationRepository::class])
class DefaultApplicationRepository(
    private val dataSource: ApplicationDataSource,
) : ApplicationRepository {

    override suspend fun applyToVacancy(vacancyId: Long, coverLetter: String?) = runCatching {
        val request = CreateApplicationRequest(
            vacancyId = vacancyId,
            coverLetter = coverLetter?.takeIf { it.isNotBlank() }
        )
        when (val response = dataSource.createApplication(request)) {
            is NetworkResponse.Success -> Unit
            is NetworkResponse.Error -> throw Exception(response.error)
        }
    }

    override suspend fun isAppliedToVacancy(vacancyId: Long) = runCatching {
        when (val response = dataSource.hasApplied(vacancyId)) {
            is NetworkResponse.Success -> response.data.hasApplied
            is NetworkResponse.Error -> false
        }
    }

    override suspend fun getApplicationsByVacancy(
        vacancyId: Long
    ): Result<List<ApplicationDetails>> = runCatching {
        when (val response = dataSource.getApplicationsByVacancy(
            vacancyId = vacancyId,
            limit = DEFAULT_LIMIT,
            offset = DEFAULT_OFFSET
        )) {
            is NetworkResponse.Success -> response.data.applications.map { it.toDomain() }
            is NetworkResponse.Error -> throw Exception(response.error)
        }
    }

    override suspend fun getSeekerApplications(): Result<List<ApplicationDetails>> = runCatching {
        when (val response = dataSource.getSeekerApplications(
            limit = DEFAULT_LIMIT,
            offset = DEFAULT_OFFSET
        )) {
            is NetworkResponse.Success -> response.data.applications.map { it.toDomain() }
            is NetworkResponse.Error -> throw Exception(response.error)
        }
    }

    override suspend fun getApplicationDetails(
        id: Long
    ): Result<ApplicationDetails> = runCatching {
        when (val response = dataSource.getApplicationDetails(id)) {
            is NetworkResponse.Success -> response.data.toDomain()
            is NetworkResponse.Error -> throw Exception(response.error)
        }
    }

    override suspend fun updateApplicationStatus(
        id: Long,
        status: ApplicationStatus
    ): Result<Unit> = runCatching {
        val request = UpdateApplicationStatusRequest(status = status.toRequest())
        when (val response = dataSource.updateApplicationStatus(id, request)) {
            is NetworkResponse.Success -> Unit
            is NetworkResponse.Error -> throw Exception(response.error)
        }
    }

    private companion object {
        const val DEFAULT_LIMIT = 20
        const val DEFAULT_OFFSET = 0
    }
}
