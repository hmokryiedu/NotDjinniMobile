@file:OptIn(ExperimentalTime::class)

package not.djinni.data.mapper

import not.djinni.network.seeker.response.SeekerProfileResponse
import not.djinni.network.seeker.response.WorkExperienceResponse
import not.djinni.network.vacancy.response.JobCategoryCodeResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class SeekerProfileMapperTest {

    @Test
    fun toDomain_maps_work_experience_with_nullable_end_date_and_is_current() {
        val startDate = Instant.parse("2024-01-01T00:00:00Z")
        val response = SeekerProfileResponse(
            id = 1L,
            aboutMe = "about",
            speciality = "Android",
            desiredSalary = 3000,
            experienceYears = 4,
            jobCategory = JobCategoryCodeResponse.SOFTWARE_DEV,
            workExperience = listOf(
                WorkExperienceResponse(
                    id = 10L,
                    companyName = "Acme",
                    position = "Android Dev",
                    description = null,
                    startDate = startDate,
                    endDate = null,
                    isCurrent = true,
                )
            ),
        )

        val profile = response.toDomain()

        assertEquals(1, profile.workExperience.size)
        val workExperience = profile.workExperience.first()
        assertEquals(10L, workExperience.id)
        assertTrue(workExperience.isCurrent)
        assertNull(workExperience.endDate)
    }
}
