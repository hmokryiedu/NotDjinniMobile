package not.djinni.network.employer

import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.employer.request.CreateEmployerProfileRequest
import not.djinni.network.employer.request.UpdateEmployerProfileRequest
import not.djinni.network.employer.response.EmployerProfileResponse
import not.djinni.network.vacancy.response.VacancyListResponse

interface EmployerDataSource {

    suspend fun getProfile(): NetworkResponse<EmployerProfileResponse>

    suspend fun createProfile(request: CreateEmployerProfileRequest): NetworkResponse<EmployerProfileResponse>
    suspend fun updateProfileRole(request: UpdateEmployerProfileRequest): NetworkResponse<EmployerProfileResponse>

    suspend fun getVacancies(
        limit: Int = 20,
        offset: Int = 0,
        search: String? = null,
    ): NetworkResponse<VacancyListResponse>
}
