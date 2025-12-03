package not.djinni.network.application

import io.ktor.client.HttpClient
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.setBody
import not.djinni.network.application.request.CreateApplicationRequest
import not.djinni.network.application.resource.Application
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
}
