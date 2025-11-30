package not.djinni.network.seeker

import io.ktor.client.HttpClient
import io.ktor.client.plugins.resources.get
import not.djinni.network.common.extension.networkResponse
import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.seeker.response.SeekerProfileResponse
import not.djinni.network.seeker.resource.Seeker
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single(binds = [SeekerDataSource::class])
internal class DefaultSeekerDataSource(
    @Named("authenticated") private val httpClient: HttpClient
) : SeekerDataSource {

    override suspend fun getProfile(): NetworkResponse<SeekerProfileResponse> {
        return httpClient
            .get(Seeker.Profile())
            .networkResponse<SeekerProfileResponse>()
    }
}
