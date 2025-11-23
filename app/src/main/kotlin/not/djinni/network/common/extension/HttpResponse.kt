package not.djinni.network.common.extension

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import not.djinni.network.common.response.ErrorResponse
import not.djinni.network.common.response.NetworkResponse

suspend inline fun <reified T : Any> HttpResponse.networkResponse(): NetworkResponse<T> {
    return when (status.value) {
        in 200..299 -> NetworkResponse.Success(body<T>())
        else -> NetworkResponse.Error(body<ErrorResponse>().message)
    }
}