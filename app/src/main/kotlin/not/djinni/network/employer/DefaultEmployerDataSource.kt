package not.djinni.network.employer

import io.ktor.client.HttpClient
import io.ktor.client.plugins.resources.get
import not.djinni.network.common.extension.networkResponse
import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.employer.resource.Employer
import not.djinni.network.model.response.EmployerProfileResponse
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single(binds = [EmployerDataSource::class])
internal class DefaultEmployerDataSource(
    @Named("authenticated") private val httpClient: HttpClient
) : EmployerDataSource {

    override suspend fun getProfile(): NetworkResponse<EmployerProfileResponse> {
        return httpClient
            .get(Employer.Profile())
            .networkResponse<EmployerProfileResponse>()
    }
}
