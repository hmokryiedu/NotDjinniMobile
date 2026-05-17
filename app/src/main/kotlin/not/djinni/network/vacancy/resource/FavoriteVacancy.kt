package not.djinni.network.vacancy.resource

import io.ktor.resources.Resource
import kotlinx.serialization.Serializable

@Serializable
@Resource("/favorite/vacancy")
class FavoriteVacancy(
    val limit: Int? = null,
    val offset: Int? = null,
) {
    @Serializable
    @Resource("{vacancyId}")
    class ById(
        val parent: FavoriteVacancy = FavoriteVacancy(),
        val vacancyId: Long,
    )
}
