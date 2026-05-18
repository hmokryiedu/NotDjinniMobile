package not.djinni.domain.repository

import not.djinni.model.seeker.SeekerProfile
import not.djinni.model.seeker.vacancy.Vacancy

interface SeekerRepository {

    suspend fun getProfile(): SeekerProfile?
    suspend fun createProfile(profile: SeekerProfile): SeekerProfile
    suspend fun updateProfile(profile: SeekerProfile): SeekerProfile
    suspend fun getRecommendedVacancies(query: String?): Result<List<Vacancy>>
}
