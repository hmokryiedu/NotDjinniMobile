package not.djinni.network.vacancy

import not.djinni.network.common.response.NetworkResponse
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

    suspend fun getAppliedVacancies(
        limit: Int = 60,
        offset: Int = 0,
    ): NetworkResponse<VacancyListResponse>

    suspend fun createVacancy(request: CreateVacancyRequest): NetworkResponse<VacancyDetailsResponse>
}
