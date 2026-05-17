package not.djinni.network.application.resource

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Serializable
@Resource("/application")
class Application {
    @Serializable
    @Resource("check/vacancy/{vacancyId}")
    data class CheckByVacancy(val parent: Application = Application(), val vacancyId: Long)

    @Serializable
    @Resource("vacancy/{vacancyId}")
    data class ByVacancy(
        val parent: Application = Application(),
        val vacancyId: Long,
        val limit: Int? = null,
        val offset: Int? = null,
    )

    @Serializable
    @Resource("vacancy/{vacancyId}/mine")
    data class MineByVacancy(val parent: Application = Application(), val vacancyId: Long)

    @Serializable
    @Resource("{id}")
    data class Details(val parent: Application = Application(), val id: Long)

    @Serializable
    @Resource("{id}/status")
    data class UpdateStatus(val parent: Application = Application(), val id: Long)
}
