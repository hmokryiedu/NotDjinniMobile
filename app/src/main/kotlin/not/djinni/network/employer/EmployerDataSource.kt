package not.djinni.network.employer

import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.employer.request.CreateEmployerProfileRequest
import not.djinni.network.employer.response.EmployerProfileResponse

interface EmployerDataSource {

    suspend fun getProfile(): NetworkResponse<EmployerProfileResponse>

    suspend fun createProfile(request: CreateEmployerProfileRequest): NetworkResponse<EmployerProfileResponse>
}
