package not.djinni.di.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.engine.okhttp.OkHttpConfig
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.resources.Resources
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlin.time.DurationUnit
import kotlin.time.toDuration

open class BaseClientBuilder {

    open fun create(): HttpClient {
        return HttpClient(OkHttp) {
            setupEngine()
            installContentNegotiation()
            install(Resources)
            installDefaultRequest()
        }
    }

    protected fun HttpClientConfig<OkHttpConfig>.setupEngine() {
        val duration = TIMEOUT_SECONDS.toDuration(DurationUnit.SECONDS)
        engine {
            config {
                connectTimeout(duration)
                readTimeout(duration)
                writeTimeout(duration)
            }
        }
    }

    protected fun HttpClientConfig<*>.installDefaultRequest() {
        install(DefaultRequest) {
            host = "192.168.31.155"
            port = 8080
            url { protocol = URLProtocol.HTTP }
            contentType(ContentType.Application.Json)
        }
    }

    protected fun HttpClientConfig<*>.installContentNegotiation() {
        val json = Json {
            ignoreUnknownKeys = true
            prettyPrint = false
        }
        install(ContentNegotiation) { json(json) }
    }

    private companion object {
        const val TIMEOUT_SECONDS = 10
    }
}