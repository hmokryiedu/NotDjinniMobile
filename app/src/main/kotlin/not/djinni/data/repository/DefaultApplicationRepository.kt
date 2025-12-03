package not.djinni.data.repository

import not.djinni.domain.repository.ApplicationRepository
import not.djinni.network.application.ApplicationDataSource
import not.djinni.network.application.request.CreateApplicationRequest
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
}
