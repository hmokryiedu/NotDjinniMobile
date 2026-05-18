package not.djinni.network.seeker

import io.ktor.client.HttpClient
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.post
import io.ktor.client.plugins.resources.put
import io.ktor.client.request.setBody
import not.djinni.network.common.extension.networkResponse
import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.seeker.request.CreateSeekerProfileRequest
import not.djinni.network.seeker.request.UpdateSeekerProfileRequest
import not.djinni.network.seeker.resource.Seeker
import not.djinni.network.seeker.response.SeekerProfileResponse
import not.djinni.network.vacancy.response.VacancyListResponse
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

    override suspend fun createProfile(request: CreateSeekerProfileRequest): NetworkResponse<SeekerProfileResponse> {
        return httpClient
            .post(Seeker.Profile()) { setBody(request) }
            .networkResponse<SeekerProfileResponse>()
    }

    override suspend fun updateProfile(request: UpdateSeekerProfileRequest): NetworkResponse<SeekerProfileResponse> {
        return httpClient
            .put(Seeker.Profile()) { setBody(request) }
            .networkResponse<SeekerProfileResponse>()
    }

    override suspend fun getRecommendedVacancies(
        limit: Int,
        offset: Int,
        search: String?
    ): NetworkResponse<VacancyListResponse> {
        return httpClient
            .get(Seeker.Vacancy(limit = limit, offset = offset, search = search))
            .networkResponse<VacancyListResponse>()
    }
}
