package not.djinni.network.seeker

import io.ktor.client.HttpClient
import io.ktor.client.plugins.resources.delete
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.post
import io.ktor.client.plugins.resources.put
import io.ktor.client.request.setBody
import not.djinni.network.common.extension.networkResponse
import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.seeker.request.CreateCoverLetterTemplateRequest
import not.djinni.network.seeker.request.UpdateCoverLetterTemplateRequest
import not.djinni.network.seeker.resource.Template
import not.djinni.network.seeker.response.CoverLetterTemplateListResponse
import not.djinni.network.seeker.response.CoverLetterTemplateResponse
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single(binds = [CoverLetterTemplateDataSource::class])
internal class DefaultCoverLetterTemplateDataSource(
    @Named("authenticated") private val httpClient: HttpClient,
) : CoverLetterTemplateDataSource {
    override suspend fun createTemplate(
        request: CreateCoverLetterTemplateRequest,
    ): NetworkResponse<CoverLetterTemplateResponse> {
        return httpClient
            .post(Template()) { setBody(request) }
            .networkResponse<CoverLetterTemplateResponse>()
    }

    override suspend fun getTemplates(): NetworkResponse<CoverLetterTemplateListResponse> {
        return httpClient
            .get(Template())
            .networkResponse<CoverLetterTemplateListResponse>()
    }

    override suspend fun getTemplate(id: Long): NetworkResponse<CoverLetterTemplateResponse> {
        return httpClient
            .get(Template.Details(id = id))
            .networkResponse<CoverLetterTemplateResponse>()
    }

    override suspend fun updateTemplate(
        id: Long,
        request: UpdateCoverLetterTemplateRequest,
    ): NetworkResponse<Unit> {
        return httpClient
            .put(Template.Details(id = id)) { setBody(request) }
            .networkResponse<Unit>()
    }

    override suspend fun deleteTemplate(id: Long): NetworkResponse<Unit> {
        return httpClient
            .delete(Template.Details(id = id))
            .networkResponse<Unit>()
    }
}
