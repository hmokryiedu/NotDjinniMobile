package not.djinni.network.seeker

import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.seeker.request.CreateSeekerProfileRequest
import not.djinni.network.seeker.response.SeekerProfileResponse

interface SeekerDataSource {

    suspend fun getProfile(): NetworkResponse<SeekerProfileResponse>
    suspend fun createProfile(request: CreateSeekerProfileRequest): NetworkResponse<SeekerProfileResponse>
}
