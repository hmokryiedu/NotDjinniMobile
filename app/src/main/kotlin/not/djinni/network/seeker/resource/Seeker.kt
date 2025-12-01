package not.djinni.network.seeker.resource

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Serializable
@Resource("/seeker")
class Seeker {

    @Serializable
    @Resource("profile")
    class Profile(val parent: Seeker = Seeker())

    @Serializable
    @Resource("vacancy")
    class Vacancy(
        val parent: Seeker = Seeker(),
        val limit: Int? = null,
        val offset: Int? = null,
        val search: String? = null
    )
}