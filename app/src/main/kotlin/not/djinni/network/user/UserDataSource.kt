package not.djinni.network.user

import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.model.response.UserResponse

interface UserDataSource {

    suspend fun getUser(): NetworkResponse<UserResponse>
}
