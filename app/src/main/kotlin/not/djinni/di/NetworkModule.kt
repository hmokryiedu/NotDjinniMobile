package not.djinni.di

import io.ktor.client.HttpClient
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class NetworkModule {

    @Single
    fun provideHttpClient(): HttpClient = HttpClientFactory.create()
}