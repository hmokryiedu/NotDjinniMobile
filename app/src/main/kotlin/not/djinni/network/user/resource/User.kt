package not.djinni.network.user.resource

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Serializable
@Resource("/user")
class User
