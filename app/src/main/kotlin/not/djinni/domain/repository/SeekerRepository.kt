package not.djinni.domain.repository

import not.djinni.model.role.profile.SeekerProfile

interface SeekerRepository {

    suspend fun getProfile(): SeekerProfile?
}