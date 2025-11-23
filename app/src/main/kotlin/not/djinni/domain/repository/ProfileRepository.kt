package not.djinni.domain.repository

import not.djinni.model.EmployerProfile
import not.djinni.model.SeekerProfile

interface ProfileRepository {

    suspend fun getSeekerProfile(): SeekerProfile?
    suspend fun getEmployerProfile(): EmployerProfile?
}
