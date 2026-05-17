@file:OptIn(ExperimentalTime::class)

package not.djinni.model.seeker.vacancy

import not.djinni.network.employer.response.CompanyResponse
import not.djinni.network.vacancy.response.EmploymentTypeResponse
import not.djinni.network.vacancy.response.JobCategoryCodeResponse
import not.djinni.network.vacancy.response.VacancyDetailsResponse
import not.djinni.network.vacancy.response.VacancyStatusCodeResponse
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class VacancyMapperTest {

    @Test
    fun toDomain_maps_viewsCount() {
        val instant = Instant.parse("2026-05-14T10:00:00Z")
        val response = VacancyDetailsResponse(
            id = 7,
            company = CompanyResponse(
                id = 3,
                name = "Uklon",
                website = "https://uklon.example",
                description = "Mobility product company",
            ),
            title = "Java API Engineer",
            description = "Maintain APIs",
            viewsCount = 42,
            salaryMin = 4200,
            salaryMax = 6000,
            minExperienceYears = 3,
            employmentType = EmploymentTypeResponse.FULL_TIME,
            category = JobCategoryCodeResponse.SOFTWARE_DEV,
            status = VacancyStatusCodeResponse.ACTIVE,
            createdAt = instant,
            updatedAt = instant,
        )

        val vacancy = response.toDomain()

        assertEquals(42, vacancy.viewsCount)
    }
}
