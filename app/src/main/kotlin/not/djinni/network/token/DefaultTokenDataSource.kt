package not.djinni.network.token

import io.ktor.client.HttpClient
import io.ktor.client.plugins.resources.get
import not.djinni.network.common.extension.networkResponse
import not.djinni.network.common.response.MessageResponse
import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.token.resource.Token
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single(binds = [TokenDataSource::class])
class DefaultTokenDataSource(
    @Named("authenticated") private val httpClient: HttpClient
) : TokenDataSource {

    override suspend fun validate(): NetworkResponse<MessageResponse> {
        return httpClient.get(Token.Validate()).networkResponse<MessageResponse>()
    }
}