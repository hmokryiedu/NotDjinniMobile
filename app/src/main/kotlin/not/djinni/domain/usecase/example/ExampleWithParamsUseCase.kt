package not.djinni.domain.usecase.example

import not.djinni.domain.usecase.core.UseCaseWithParams
import org.koin.core.annotation.Factory

/**
 * There is return type can be any that we provide as generic for UseCaseWithParams
 * params can be extracted to data class or can be used any other type
 */

@Factory
class ExampleWithParamsUseCase : UseCaseWithParams<String, ExampleWithParamsUseCase.Params> {

    override suspend fun invoke(params: Params): String {
        return params.data
    }

    data class Params(
        val data: String
    )
}