package not.djinni.network.vacancy

import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.vacancy.response.VacancyDetailsResponse
import not.djinni.network.vacancy.response.VacancyListResponse

interface VacancyDataSource {

    suspend fun getAllVacancies(
        limit: Int = 60,
        offset: Int = 0,
        search: String?
    ): NetworkResponse<VacancyListResponse>

    suspend fun getVacancyById(id: Long): NetworkResponse<VacancyDetailsResponse>
}
