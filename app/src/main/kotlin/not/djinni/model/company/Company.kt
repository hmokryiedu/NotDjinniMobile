package not.djinni.model.company

data class Company(
    val id: Long,
    val name: String,
    val website: String?,
    val description: String
)