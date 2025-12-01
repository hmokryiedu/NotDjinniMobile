package not.djinni.model.seeker

data class SeekerProfile(
    val id: Long,
    val aboutMe: String?,
    val speciality: String,
    val desiredSalary: Int,
    val experienceYears: Int,
    val workExperience: List<WorkExperience> = emptyList()
)