package not.djinni.network.vacancy

import io.ktor.client.HttpClient
import io.ktor.client.plugins.resources.delete
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.setBody
import not.djinni.network.common.extension.networkResponse
import not.djinni.network.common.response.MessageResponse
import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.application.request.ApplicationStatusRequest
import not.djinni.network.vacancy.request.CreateVacancyRequest
import not.djinni.network.vacancy.resource.FavoriteVacancy
import not.djinni.network.vacancy.resource.Vacancy
import not.djinni.network.vacancy.response.VacancyDetailsResponse
import not.djinni.network.vacancy.response.VacancyListResponse
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single(binds = [VacancyDataSource::class])
internal class DefaultVacancyDataSource(
    @Named("authenticated") private val httpClient: HttpClient,
    @Named("public") private val publicHttpClient: HttpClient,
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

    override suspend fun getPublicVacancies(
        limit: Int,
        offset: Int,
        search: String?
    ): NetworkResponse<VacancyListResponse> {
        return publicHttpClient
            .get(Vacancy(limit = limit, offset = offset, search = search))
            .networkResponse<VacancyListResponse>()
    }

    override suspend fun getPublicVacancyById(id: Long): NetworkResponse<VacancyDetailsResponse> {
        return publicHttpClient
            .get(Vacancy.ById(id = id))
            .networkResponse<VacancyDetailsResponse>()
    }

    override suspend fun getAppliedVacancies(
        limit: Int,
        offset: Int,
        statuses: List<ApplicationStatusRequest>
    ): NetworkResponse<VacancyListResponse> {
        return httpClient
            .get(
                Vacancy.Applied(
                    limit = limit,
                    offset = offset,
                    application_status = statuses.takeIf { it.isNotEmpty() }
                )
            )
            .networkResponse<VacancyListResponse>()
    }

    override suspend fun getFavoriteVacancies(
        limit: Int,
        offset: Int
    ): NetworkResponse<VacancyListResponse> {
        return httpClient
            .get(FavoriteVacancy(limit = limit, offset = offset))
            .networkResponse<VacancyListResponse>()
    }

    override suspend fun addFavoriteVacancy(vacancyId: Long): NetworkResponse<MessageResponse> {
        return httpClient
            .post(FavoriteVacancy.ById(vacancyId = vacancyId))
            .networkResponse<MessageResponse>()
    }

    override suspend fun removeFavoriteVacancy(vacancyId: Long): NetworkResponse<MessageResponse> {
        return httpClient
            .delete(FavoriteVacancy.ById(vacancyId = vacancyId))
            .networkResponse<MessageResponse>()
    }

    override suspend fun createVacancy(
        request: CreateVacancyRequest
    ): NetworkResponse<VacancyDetailsResponse> {
        return httpClient
            .post(Vacancy()) { setBody(request) }
            .networkResponse()
    }
}
