@file:OptIn(ExperimentalTime::class)

package not.djinni.presentation.screens.seeker.profile.create

import not.djinni.model.seeker.vacancy.JobCategoryCode
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

internal sealed interface CreateSeekerProfileAction {
    data object ShowAddWorkExperienceAlert : CreateSeekerProfileAction
    data object ShowSelectJobCategoryAlert : CreateSeekerProfileAction

    data class SelectJobCategory(val category: JobCategoryCode) : CreateSeekerProfileAction

    data class CreateProfile(
        val speciality: String,
        val desiredSalary: String,
        val yearsOfExperience: String,
        val aboutMe: String,
    ) : CreateSeekerProfileAction

    data class AddWorkExperience(
        val position: String,
        val companyName: String,
        val description: String,
        val startDate: Instant?,
        val endDate: Instant?,
    ) : CreateSeekerProfileAction

    data object HideAlert : CreateSeekerProfileAction
}
