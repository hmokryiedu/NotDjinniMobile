package not.djinni.data.mapper

import not.djinni.model.User
import not.djinni.network.model.response.UserResponse

internal fun UserResponse.toDomain(): User {
    return User(
        id = id,
        email = email,
        name = name
    )
}
