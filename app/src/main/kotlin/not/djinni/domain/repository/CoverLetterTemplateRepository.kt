package not.djinni.domain.repository

import not.djinni.model.seeker.coverletter.CoverLetterTemplate

interface CoverLetterTemplateRepository {
    suspend fun createTemplate(message: String): Result<CoverLetterTemplate>
    suspend fun getTemplates(): Result<List<CoverLetterTemplate>>
    suspend fun getTemplate(id: Long): Result<CoverLetterTemplate>
    suspend fun updateTemplate(id: Long, message: String): Result<Unit>
    suspend fun deleteTemplate(id: Long): Result<Unit>
}
