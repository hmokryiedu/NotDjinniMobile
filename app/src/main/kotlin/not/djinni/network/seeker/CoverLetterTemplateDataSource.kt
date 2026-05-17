package not.djinni.network.seeker

import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.seeker.request.CreateCoverLetterTemplateRequest
import not.djinni.network.seeker.request.UpdateCoverLetterTemplateRequest
import not.djinni.network.seeker.response.CoverLetterTemplateListResponse
import not.djinni.network.seeker.response.CoverLetterTemplateResponse

interface CoverLetterTemplateDataSource {
    suspend fun createTemplate(request: CreateCoverLetterTemplateRequest): NetworkResponse<CoverLetterTemplateResponse>
    suspend fun getTemplates(): NetworkResponse<CoverLetterTemplateListResponse>
    suspend fun getTemplate(id: Long): NetworkResponse<CoverLetterTemplateResponse>
    suspend fun updateTemplate(id: Long, request: UpdateCoverLetterTemplateRequest): NetworkResponse<Unit>
    suspend fun deleteTemplate(id: Long): NetworkResponse<Unit>
}
