package not.djinni.network.application

import io.ktor.client.HttpClient
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.patch
import io.ktor.client.plugins.resources.post
import io.ktor.client.plugins.resources.put
import io.ktor.client.request.setBody
import not.djinni.network.application.request.CreateApplicationRequest
import not.djinni.network.application.request.UpdateApplicationStatusRequest
import not.djinni.network.application.resource.Application
import not.djinni.network.application.response.ApplicationDetailsListResponse
import not.djinni.network.application.response.ApplicationDetailsResponse
import not.djinni.network.application.response.ApplicationResponse
import not.djinni.network.application.response.HasAppliedResponse
import not.djinni.network.common.extension.networkResponse
import not.djinni.network.common.response.NetworkResponse
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single(binds = [ApplicationDataSource::class])
internal class DefaultApplicationDataSource(
    @Named("authenticated") private val httpClient: HttpClient
) : ApplicationDataSource {

    override suspend fun createApplication(
        request: CreateApplicationRequest
    ): NetworkResponse<ApplicationResponse> {
        return httpClient
            .post(Application()) { setBody(request) }
            .networkResponse<ApplicationResponse>()
    }

    override suspend fun hasApplied(
        vacancyId: Long
    ): NetworkResponse<HasAppliedResponse> {
        return httpClient
            .get(Application.CheckByVacancy(vacancyId = vacancyId))
            .networkResponse<HasAppliedResponse>()
    }

    override suspend fun getApplicationsByVacancy(
        vacancyId: Long,
        limit: Int,
        offset: Int
    ): NetworkResponse<ApplicationDetailsListResponse> {
        return httpClient
            .get(Application.ByVacancy(vacancyId = vacancyId, limit = limit, offset = offset))
            .networkResponse<ApplicationDetailsListResponse>()
    }

    override suspend fun getSeekerApplications(
        limit: Int,
        offset: Int
    ): NetworkResponse<ApplicationDetailsListResponse> {
        return httpClient
            .get(Application())
            .networkResponse<ApplicationDetailsListResponse>()
    }

    override suspend fun getMyApplicationByVacancy(
        vacancyId: Long
    ): NetworkResponse<ApplicationDetailsResponse> {
        return httpClient
            .get(Application.MineByVacancy(vacancyId = vacancyId))
            .networkResponse<ApplicationDetailsResponse>()
    }

    override suspend fun getApplicationDetails(
        id: Long
    ): NetworkResponse<ApplicationDetailsResponse> {
        return httpClient
            .get(Application.Details(id = id))
            .networkResponse<ApplicationDetailsResponse>()
    }

    override suspend fun updateApplicationStatus(
        id: Long,
        request: UpdateApplicationStatusRequest
    ): NetworkResponse<Unit> {
        return httpClient
            .put(Application.UpdateStatus(id = id)) { setBody(request) }
            .networkResponse<Unit>()
    }

    override suspend fun withdrawApplication(id: Long): NetworkResponse<Unit> {
        return httpClient
            .patch(Application.Withdraw(id = id))
            .networkResponse<Unit>()
    }
}
