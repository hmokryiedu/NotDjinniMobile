package not.djinni.network.vacancy

import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.common.response.MessageResponse
import not.djinni.network.application.request.ApplicationStatusRequest
import not.djinni.network.vacancy.request.CreateVacancyRequest
import not.djinni.network.vacancy.response.VacancyDetailsResponse
import not.djinni.network.vacancy.response.VacancyListResponse

interface VacancyDataSource {

    suspend fun getAllVacancies(
        limit: Int = 60,
        offset: Int = 0,
        search: String?
    ): NetworkResponse<VacancyListResponse>

    suspend fun getVacancyById(id: Long): NetworkResponse<VacancyDetailsResponse>

    suspend fun getPublicVacancies(
        limit: Int = 60,
        offset: Int = 0,
        search: String?
    ): NetworkResponse<VacancyListResponse>

    suspend fun getPublicVacancyById(id: Long): NetworkResponse<VacancyDetailsResponse>

    suspend fun getAppliedVacancies(
        limit: Int = 60,
        offset: Int = 0,
        statuses: List<ApplicationStatusRequest> = emptyList(),
    ): NetworkResponse<VacancyListResponse>

    suspend fun getFavoriteVacancies(
        limit: Int = 60,
        offset: Int = 0,
    ): NetworkResponse<VacancyListResponse>

    suspend fun addFavoriteVacancy(vacancyId: Long): NetworkResponse<MessageResponse>

    suspend fun removeFavoriteVacancy(vacancyId: Long): NetworkResponse<MessageResponse>

    suspend fun createVacancy(request: CreateVacancyRequest): NetworkResponse<VacancyDetailsResponse>
}
