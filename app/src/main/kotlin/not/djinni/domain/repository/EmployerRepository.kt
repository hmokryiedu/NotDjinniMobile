package not.djinni.domain.repository

import not.djinni.model.employer.EmployerProfile
import not.djinni.model.seeker.vacancy.Vacancy

interface EmployerRepository {

    suspend fun getProfile(): EmployerProfile?

    suspend fun createProfile(companyId: Long, role: String): EmployerProfile

    suspend fun getEmployerVacancies(): Result<List<Vacancy>>
}