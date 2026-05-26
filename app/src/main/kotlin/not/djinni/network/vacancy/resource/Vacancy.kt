package not.djinni.network.vacancy.resource

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable
import not.djinni.network.application.request.ApplicationStatusRequest

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

    @Serializable
    @Resource("applied")
    class Applied(
        val parent: Vacancy = Vacancy(),
        val limit: Int? = null,
        val offset: Int? = null,
        val application_status: List<ApplicationStatusRequest>? = null,
    )
}
