package not.djinni.network.profile

import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.model.response.EmployerProfileResponse
import not.djinni.network.model.response.SeekerProfileResponse

interface ProfileDataSource {

    suspend fun getSeekerProfile(): NetworkResponse<SeekerProfileResponse>
    suspend fun getEmployerProfile(): NetworkResponse<EmployerProfileResponse>
}
