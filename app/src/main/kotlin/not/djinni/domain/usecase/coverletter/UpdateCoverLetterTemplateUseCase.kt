package not.djinni.domain.usecase.coverletter

import not.djinni.domain.repository.CoverLetterTemplateRepository
import not.djinni.domain.usecase.core.UseCaseWithParams
import org.koin.core.annotation.Factory

@Factory
class UpdateCoverLetterTemplateUseCase(
    private val repository: CoverLetterTemplateRepository,
) : UseCaseWithParams<Result<Unit>, UpdateCoverLetterTemplateUseCase.Params> {
    override suspend fun invoke(params: Params): Result<Unit> {
        return repository.updateTemplate(params.id, params.message)
    }

    data class Params(
        val id: Long,
        val message: String,
    )
}
