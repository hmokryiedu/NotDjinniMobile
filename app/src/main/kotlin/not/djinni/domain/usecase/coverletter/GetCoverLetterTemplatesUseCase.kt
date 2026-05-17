package not.djinni.domain.usecase.coverletter

import not.djinni.domain.repository.CoverLetterTemplateRepository
import not.djinni.domain.usecase.core.UseCase
import not.djinni.model.seeker.coverletter.CoverLetterTemplate
import org.koin.core.annotation.Factory

@Factory
class GetCoverLetterTemplatesUseCase(
    private val repository: CoverLetterTemplateRepository,
) : UseCase<Result<List<CoverLetterTemplate>>> {
    override suspend fun invoke(): Result<List<CoverLetterTemplate>> {
        return repository.getTemplates()
    }
}
