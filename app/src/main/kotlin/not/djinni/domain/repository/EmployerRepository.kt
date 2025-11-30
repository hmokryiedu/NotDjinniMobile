package not.djinni.domain.repository

import not.djinni.model.role.profile.EmployerProfile

interface EmployerRepository {

    suspend fun getProfile(): EmployerProfile?
}