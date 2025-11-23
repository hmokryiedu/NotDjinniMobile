package not.djinni.network.auth.resource

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Serializable
@Resource("/auth")
class Auth {

    @Serializable
    @Resource("login")
    class Login(val parent: Auth = Auth())

    @Serializable
    @Resource("register")
    class Register(val parent: Auth = Auth())
}