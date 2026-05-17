package not.djinni.domain.usecase.coverletter

import not.djinni.domain.repository.CoverLetterTemplateRepository
import not.djinni.domain.usecase.core.UseCaseWithParams
import org.koin.core.annotation.Factory

@Factory
class DeleteCoverLetterTemplateUseCase(
    private val repository: CoverLetterTemplateRepository,
) : UseCaseWithParams<Result<Unit>, Long> {
    override suspend fun invoke(params: Long): Result<Unit> {
        return repository.deleteTemplate(params)
    }
}
