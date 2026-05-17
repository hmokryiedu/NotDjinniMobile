package not.djinni.data.repository

import not.djinni.domain.repository.CoverLetterTemplateRepository
import not.djinni.model.seeker.coverletter.CoverLetterTemplate
import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.seeker.CoverLetterTemplateDataSource
import not.djinni.network.seeker.request.CreateCoverLetterTemplateRequest
import not.djinni.network.seeker.request.UpdateCoverLetterTemplateRequest
import not.djinni.network.seeker.response.CoverLetterTemplateResponse
import org.koin.core.annotation.Single

@Single(binds = [CoverLetterTemplateRepository::class])
class DefaultCoverLetterTemplateRepository(
    private val dataSource: CoverLetterTemplateDataSource,
) : CoverLetterTemplateRepository {
    override suspend fun createTemplate(message: String): Result<CoverLetterTemplate> = runCatching {
        when (val response = dataSource.createTemplate(CreateCoverLetterTemplateRequest(message = message))) {
            is NetworkResponse.Success -> response.data.toDomain()
            is NetworkResponse.Error -> throw Exception(response.error)
        }
    }

    override suspend fun getTemplates(): Result<List<CoverLetterTemplate>> = runCatching {
        when (val response = dataSource.getTemplates()) {
            is NetworkResponse.Success -> response.data.templates.map(CoverLetterTemplateResponse::toDomain)
            is NetworkResponse.Error -> throw Exception(response.error)
        }
    }

    override suspend fun getTemplate(id: Long): Result<CoverLetterTemplate> = runCatching {
        when (val response = dataSource.getTemplate(id)) {
            is NetworkResponse.Success -> response.data.toDomain()
            is NetworkResponse.Error -> throw Exception(response.error)
        }
    }

    override suspend fun updateTemplate(id: Long, message: String): Result<Unit> = runCatching {
        when (val response = dataSource.updateTemplate(id, UpdateCoverLetterTemplateRequest(message = message))) {
            is NetworkResponse.Success -> Unit
            is NetworkResponse.Error -> throw Exception(response.error)
        }
    }

    override suspend fun deleteTemplate(id: Long): Result<Unit> = runCatching {
        when (val response = dataSource.deleteTemplate(id)) {
            is NetworkResponse.Success -> Unit
            is NetworkResponse.Error -> throw Exception(response.error)
        }
    }
}

private fun CoverLetterTemplateResponse.toDomain(): CoverLetterTemplate {
    return CoverLetterTemplate(
        id = id,
        message = message,
    )
}
