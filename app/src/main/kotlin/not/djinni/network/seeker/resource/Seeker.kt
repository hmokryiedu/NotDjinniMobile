package not.djinni.network.seeker.resource

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Serializable
@Resource("/seeker")
class Seeker {

    @Serializable
    @Resource("profile")
    class Profile(val parent: Seeker = Seeker())
}