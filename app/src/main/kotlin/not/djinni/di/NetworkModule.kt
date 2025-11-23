package not.djinni.di

import io.ktor.client.HttpClient
import not.djinni.di.client.AuthenticatedClientBuilder
import not.djinni.di.client.BaseClientBuilder
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Module
class NetworkModule {

    @Single
    @Named("public")
    fun provideHttpClient(): HttpClient = BaseClientBuilder().create()

    @Single
    @Named("authenticated")
    fun provideAuthenticatedHttpClient(clientBuilder: AuthenticatedClientBuilder): HttpClient {
        return clientBuilder.create()
    }
}