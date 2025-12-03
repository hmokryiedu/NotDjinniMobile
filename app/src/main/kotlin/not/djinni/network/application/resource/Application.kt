package not.djinni.network.application.resource

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Serializable
@Resource("/application")
class Application {
    @Serializable
    @Resource("check/vacancy/{vacancyId}")
    data class CheckByVacancy(val parent: Application = Application(), val vacancyId: Long)
}
