package not.djinni.network.vacancy.resource

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Serializable
@Resource("/vacancy")
class Vacancy(
    val limit: Int? = null,
    val offset: Int? = null,
    val search: String? = null
) {
    @Serializable
    @Resource("{id}")
    class ById(val parent: Vacancy = Vacancy(), val id: Long)
}
