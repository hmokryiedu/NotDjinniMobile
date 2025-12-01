package not.djinni.network.token

import not.djinni.network.common.response.MessageResponse
import not.djinni.network.common.response.NetworkResponse

interface TokenDataSource {

    suspend fun validate(): NetworkResponse<MessageResponse>
}