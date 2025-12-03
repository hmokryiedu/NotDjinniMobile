package not.djinni.network.application

import not.djinni.network.application.request.CreateApplicationRequest
import not.djinni.network.application.response.ApplicationResponse
import not.djinni.network.application.response.HasAppliedResponse
import not.djinni.network.common.response.NetworkResponse

interface ApplicationDataSource {
    suspend fun createApplication(request: CreateApplicationRequest): NetworkResponse<ApplicationResponse>
    suspend fun hasApplied(vacancyId: Long): NetworkResponse<HasAppliedResponse>
}
