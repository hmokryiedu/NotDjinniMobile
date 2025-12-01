package not.djinni.model.seeker

import not.djinni.model.seeker.vacancy.JobCategoryCode

data class SeekerProfile(
    val id: Long,
    val aboutMe: String?,
    val speciality: String,
    val desiredSalary: Int,
    val experienceYears: Int,
    val jobCategory: JobCategoryCode,
    val workExperience: List<WorkExperience> = emptyList()
)