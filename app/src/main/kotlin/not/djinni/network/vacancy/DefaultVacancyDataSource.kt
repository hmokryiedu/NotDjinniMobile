package not.djinni.network.vacancy

import io.ktor.client.HttpClient
import io.ktor.client.plugins.resources.get
import not.djinni.network.common.extension.networkResponse
import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.vacancy.resource.Vacancy
import not.djinni.network.vacancy.response.VacancyDetailsResponse
import not.djinni.network.vacancy.response.VacancyListResponse
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single(binds = [VacancyDataSource::class])
internal class DefaultVacancyDataSource(
    @Named("authenticated") private val httpClient: HttpClient
) : VacancyDataSource {

    override suspend fun getAllVacancies(
        limit: Int,
        offset: Int,
        search: String?
    ): NetworkResponse<VacancyListResponse> {
        return httpClient
            .get(Vacancy(limit = limit, offset = offset, search = search))
            .networkResponse<VacancyListResponse>()
    }

    override suspend fun getVacancyById(id: Long): NetworkResponse<VacancyDetailsResponse> {
        return httpClient
            .get(Vacancy.ById(id = id))
            .networkResponse<VacancyDetailsResponse>()
    }
}
