package not.djinni.domain.repository

import not.djinni.model.seeker.SeekerProfile

interface SeekerRepository {

    suspend fun getProfile(): SeekerProfile?
}