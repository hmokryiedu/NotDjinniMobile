package not.djinni.domain.repository

import not.djinni.model.application.ApplicationDetails
import not.djinni.model.application.ApplicationStatus

interface ApplicationRepository {
    suspend fun applyToVacancy(vacancyId: Long, coverLetter: String?): Result<Unit>
    suspend fun isAppliedToVacancy(vacancyId: Long): Result<Boolean>
    suspend fun getApplicationsByVacancy(vacancyId: Long): Result<List<ApplicationDetails>>
    suspend fun getSeekerApplications(): Result<List<ApplicationDetails>>
    suspend fun getMyApplicationByVacancy(vacancyId: Long): Result<ApplicationDetails>
    suspend fun getApplicationDetails(id: Long): Result<ApplicationDetails>
    suspend fun updateApplicationStatus(id: Long, status: ApplicationStatus): Result<Unit>
    suspend fun withdrawApplication(id: Long): Result<Unit>
}
