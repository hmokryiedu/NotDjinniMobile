package not.djinni.domain.repository

interface TokenRepository {

    suspend fun validate(): Boolean
}