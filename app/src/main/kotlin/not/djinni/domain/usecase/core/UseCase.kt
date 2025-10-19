package not.djinni.domain.usecase.core

interface UseCase<out T : Any> {
    suspend operator fun invoke(): T
}
