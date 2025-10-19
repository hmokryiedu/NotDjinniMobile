package not.djinni.domain.usecase.example

import not.djinni.domain.usecase.core.UseCase
import org.koin.core.annotation.Factory

/**
 * There is return type can be any that we provide as generic for UseCase
 */

@Factory
class ExampleUseCase : UseCase<String> {

    override suspend fun invoke(): String {
        return ""
    }
}