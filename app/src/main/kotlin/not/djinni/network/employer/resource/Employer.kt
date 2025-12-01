package not.djinni.network.employer.resource

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Serializable
@Resource("/employer")
class Employer {

    @Serializable
    @Resource("profile")
    class Profile(val parent: Employer = Employer())

    @Serializable
    @Resource("vacancies")
    class Vacancies(
        val parent: Employer = Employer(),
        val limit: Int? = null,
        val offset: Int? = null,
    )
}