package not.djinni.network.employer

import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.employer.response.EmployerProfileResponse

interface EmployerDataSource {

    suspend fun getProfile(): NetworkResponse<EmployerProfileResponse>
}
