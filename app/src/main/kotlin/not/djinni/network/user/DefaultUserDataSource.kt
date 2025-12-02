package not.djinni.network.user

import io.ktor.client.HttpClient
import io.ktor.client.plugins.resources.get
import not.djinni.network.common.extension.networkResponse
import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.model.response.UserResponse
import not.djinni.network.user.resource.User
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single(binds = [UserDataSource::class])
internal class DefaultUserDataSource(
    @Named("authenticated") private val httpClient: HttpClient
) : UserDataSource {

    override suspend fun getUser(): NetworkResponse<UserResponse> {
        return httpClient
            .get(User())
            .networkResponse<UserResponse>()
    }
}
