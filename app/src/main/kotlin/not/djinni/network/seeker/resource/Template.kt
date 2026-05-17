package not.djinni.network.seeker.resource

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Serializable
@Resource("/seeker/templates")
class Template {
    @Serializable
    @Resource("{id}")
    data class Details(val parent: Template = Template(), val id: Long)
}
