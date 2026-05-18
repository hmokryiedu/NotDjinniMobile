package not.djinni.network.seeker

import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.seeker.request.CreateSeekerProfileRequest
import not.djinni.network.seeker.request.UpdateSeekerProfileRequest
import not.djinni.network.seeker.response.SeekerProfileResponse
import not.djinni.network.vacancy.response.VacancyListResponse

interface SeekerDataSource {

    suspend fun getProfile(): NetworkResponse<SeekerProfileResponse>
    suspend fun createProfile(request: CreateSeekerProfileRequest): NetworkResponse<SeekerProfileResponse>
    suspend fun updateProfile(request: UpdateSeekerProfileRequest): NetworkResponse<SeekerProfileResponse>
    suspend fun getRecommendedVacancies(
        limit: Int = 60,
        offset: Int = 0,
        search: String?
    ): NetworkResponse<VacancyListResponse>
}
