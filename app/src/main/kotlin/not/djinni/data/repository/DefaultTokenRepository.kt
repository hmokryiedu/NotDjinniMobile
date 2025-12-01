package not.djinni.data.repository

import not.djinni.datastore.session.SessionDataStore
import not.djinni.domain.repository.TokenRepository
import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.token.TokenDataSource
import org.koin.core.annotation.Single

@Single(binds = [TokenRepository::class])
class DefaultTokenRepository(
    private val tokenDataSource: TokenDataSource,
    private val sessionDataStore: SessionDataStore
) : TokenRepository {

    override suspend fun validate(): Boolean {
        return tokenDataSource.validate() is NetworkResponse.Success
    }
}