package not.djinni.domain.repository

import not.djinni.model.employer.EmployerProfile

interface EmployerRepository {

    suspend fun getProfile(): EmployerProfile?

    suspend fun createProfile(companyId: Long, role: String): EmployerProfile
}