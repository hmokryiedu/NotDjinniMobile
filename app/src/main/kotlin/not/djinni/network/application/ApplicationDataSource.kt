package not.djinni.network.application

import not.djinni.network.application.request.CreateApplicationRequest
import not.djinni.network.application.request.UpdateApplicationStatusRequest
import not.djinni.network.application.response.ApplicationDetailsListResponse
import not.djinni.network.application.response.ApplicationDetailsResponse
import not.djinni.network.application.response.ApplicationResponse
import not.djinni.network.application.response.HasAppliedResponse
import not.djinni.network.common.response.NetworkResponse

interface ApplicationDataSource {
    suspend fun createApplication(request: CreateApplicationRequest): NetworkResponse<ApplicationResponse>
    suspend fun hasApplied(vacancyId: Long): NetworkResponse<HasAppliedResponse>
    suspend fun getApplicationsByVacancy(
        vacancyId: Long,
        limit: Int,
        offset: Int
    ): NetworkResponse<ApplicationDetailsListResponse>
    suspend fun getSeekerApplications(
        limit: Int,
        offset: Int
    ): NetworkResponse<ApplicationDetailsListResponse>
    suspend fun getMyApplicationByVacancy(vacancyId: Long): NetworkResponse<ApplicationDetailsResponse>
    suspend fun getApplicationDetails(id: Long): NetworkResponse<ApplicationDetailsResponse>
    suspend fun updateApplicationStatus(
        id: Long,
        request: UpdateApplicationStatusRequest
    ): NetworkResponse<Unit>
}
