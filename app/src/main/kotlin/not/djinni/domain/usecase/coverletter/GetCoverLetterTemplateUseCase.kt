package not.djinni.domain.usecase.coverletter

import not.djinni.domain.repository.CoverLetterTemplateRepository
import not.djinni.domain.usecase.core.UseCaseWithParams
import not.djinni.model.seeker.coverletter.CoverLetterTemplate
import org.koin.core.annotation.Factory

@Factory
class GetCoverLetterTemplateUseCase(
    private val repository: CoverLetterTemplateRepository,
) : UseCaseWithParams<Result<CoverLetterTemplate>, Long> {
    override suspend fun invoke(params: Long): Result<CoverLetterTemplate> {
        return repository.getTemplate(params)
    }
}
