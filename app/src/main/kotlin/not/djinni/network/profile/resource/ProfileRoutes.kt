package not.djinni.network.profile.resource

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Serializable
@Resource("/profile")
class Profile {

    @Serializable
    @Resource("seeker")
    class Seeker(val parent: Profile = Profile())

    @Serializable
    @Resource("employer")
    class Employer(val parent: Profile = Profile())
}
