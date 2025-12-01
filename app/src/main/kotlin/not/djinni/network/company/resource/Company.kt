package not.djinni.network.company.resource

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Serializable
@Resource("/company")
class Company {

    @Serializable
    @Resource("search")
    class Search(
        val parent: Company = Company(),
        val name: String
    )
}
