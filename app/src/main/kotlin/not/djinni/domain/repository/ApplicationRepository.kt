package not.djinni.domain.repository

interface ApplicationRepository {
    suspend fun applyToVacancy(vacancyId: Long, coverLetter: String?): Result<Unit>
    suspend fun isAppliedToVacancy(vacancyId: Long): Result<Boolean>
}
