package not.djinni.model.role.profile

data class SeekerProfile(
    val id: Long,
    val speciality: String,
    val experienceYears: Int,
    val desiredSalary: Int,
    val aboutMe: String?
)
