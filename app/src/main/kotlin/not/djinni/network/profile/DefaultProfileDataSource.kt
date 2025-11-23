package not.djinni.network.profile

import io.ktor.client.HttpClient
import io.ktor.client.plugins.resources.get
import not.djinni.network.common.extension.networkResponse
import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.model.response.EmployerProfileResponse
import not.djinni.network.model.response.SeekerProfileResponse
import not.djinni.network.profile.resource.Profile
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single(binds = [ProfileDataSource::class])
internal class DefaultProfileDataSource(
    @Named("authenticated") private val httpClient: HttpClient
) : ProfileDataSource {

    override suspend fun getSeekerProfile(): NetworkResponse<SeekerProfileResponse> {
        return httpClient
            .get(Profile.Seeker())
            .networkResponse<SeekerProfileResponse>()
    }

    override suspend fun getEmployerProfile(): NetworkResponse<EmployerProfileResponse> {
        return httpClient
            .get(Profile.Employer())
            .networkResponse<EmployerProfileResponse>()
    }
}
