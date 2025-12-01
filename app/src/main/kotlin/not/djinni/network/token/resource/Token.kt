package not.djinni.network.token.resource

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Serializable
@Resource("/token")
class Token {

    @Serializable
    @Resource("validate")
    class Validate(val parent: Token = Token())
}