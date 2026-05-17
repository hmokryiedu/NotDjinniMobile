package not.djinni.domain.usecase.coverletter

import not.djinni.domain.repository.CoverLetterTemplateRepository
import not.djinni.domain.usecase.core.UseCaseWithParams
import not.djinni.model.seeker.coverletter.CoverLetterTemplate
import org.koin.core.annotation.Factory

@Factory
class CreateCoverLetterTemplateUseCase(
    private val repository: CoverLetterTemplateRepository,
) : UseCaseWithParams<Result<CoverLetterTemplate>, String> {
    override suspend fun invoke(params: String): Result<CoverLetterTemplate> {
        return repository.createTemplate(params)
    }
}
