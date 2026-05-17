@file:OptIn(ExperimentalTime::class)

package not.djinni.data.repository

import kotlinx.coroutines.runBlocking
import not.djinni.model.application.ApplicationStatus
import not.djinni.network.application.ApplicationDataSource
import not.djinni.network.application.request.CreateApplicationRequest
import not.djinni.network.application.request.UpdateApplicationStatusRequest
import not.djinni.network.application.response.ApplicationDetailsListResponse
import not.djinni.network.application.response.ApplicationDetailsResponse
import not.djinni.network.application.response.ApplicationResponse
import not.djinni.network.application.response.ApplicationStatusResponse
import not.djinni.network.application.response.HasAppliedResponse
import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.employer.response.CompanyResponse
import not.djinni.network.seeker.response.SeekerProfileResponse
import not.djinni.network.vacancy.response.EmploymentTypeResponse
import not.djinni.network.vacancy.response.JobCategoryCodeResponse
import not.djinni.network.vacancy.response.VacancyDetailsResponse
import not.djinni.network.vacancy.response.VacancyStatusCodeResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class DefaultApplicationRepositoryTest {

    @Test
    fun getMyApplicationByVacancy_maps_application_details() = runBlocking {
        val dataSource = FakeApplicationDataSource(
            mineByVacancyResponse = NetworkResponse.Success(applicationDetailsResponse(id = 42))
        )
        val repository = DefaultApplicationRepository(dataSource)

        val result = repository.getMyApplicationByVacancy(vacancyId = 7)

        assertTrue(result.isSuccess)
        val application = result.getOrThrow()
        assertEquals(42L, application.id)
        assertEquals(7L, dataSource.requestedMineVacancyId)
        assertEquals(ApplicationStatus.APPLIED, application.status)
        assertEquals("Java API Engineer", application.vacancy.title)
    }

    @Test
    fun getMyApplicationByVacancy_returns_failure_on_error_response() = runBlocking {
        val dataSource = FakeApplicationDataSource(
            mineByVacancyResponse = NetworkResponse.Error("not found")
        )
        val repository = DefaultApplicationRepository(dataSource)

        val result = repository.getMyApplicationByVacancy(vacancyId = 7)

        assertTrue(result.isFailure)
        assertEquals("not found", result.exceptionOrNull()?.message)
    }

    private class FakeApplicationDataSource(
        private val mineByVacancyResponse: NetworkResponse<ApplicationDetailsResponse>,
    ) : ApplicationDataSource {
        var requestedMineVacancyId: Long? = null

        override suspend fun createApplication(
            request: CreateApplicationRequest,
        ): NetworkResponse<ApplicationResponse> = error("Not used")

        override suspend fun hasApplied(vacancyId: Long): NetworkResponse<HasAppliedResponse> =
            error("Not used")

        override suspend fun getApplicationsByVacancy(
            vacancyId: Long,
            limit: Int,
            offset: Int,
        ): NetworkResponse<ApplicationDetailsListResponse> = error("Not used")

        override suspend fun getSeekerApplications(
            limit: Int,
            offset: Int,
        ): NetworkResponse<ApplicationDetailsListResponse> = error("Not used")

        override suspend fun getMyApplicationByVacancy(
            vacancyId: Long,
        ): NetworkResponse<ApplicationDetailsResponse> {
            requestedMineVacancyId = vacancyId
            return mineByVacancyResponse
        }

        override suspend fun getApplicationDetails(id: Long): NetworkResponse<ApplicationDetailsResponse> =
            error("Not used")

        override suspend fun updateApplicationStatus(
            id: Long,
            request: UpdateApplicationStatusRequest,
        ): NetworkResponse<Unit> = error("Not used")
    }

    private fun applicationDetailsResponse(id: Long): ApplicationDetailsResponse {
        val instant = Instant.parse("2026-05-14T10:00:00Z")
        return ApplicationDetailsResponse(
            id = id,
            vacancy = VacancyDetailsResponse(
                id = 7,
                company = CompanyResponse(
                    id = 3,
                    name = "Uklon",
                    website = "https://uklon.example",
                    description = "Mobility product company",
                ),
                title = "Java API Engineer",
                description = "Maintain APIs",
                salaryMin = 4200,
                salaryMax = 6000,
                minExperienceYears = 3,
                employmentType = EmploymentTypeResponse.FULL_TIME,
                category = JobCategoryCodeResponse.SOFTWARE_DEV,
                status = VacancyStatusCodeResponse.ACTIVE,
                createdAt = instant,
                updatedAt = instant,
            ),
            jobSeeker = SeekerProfileResponse(
                id = 6,
                aboutMe = "Backend Kotlin Developer",
                speciality = "Backend Kotlin Developer",
                desiredSalary = 4200,
                experienceYears = 4,
                jobCategory = JobCategoryCodeResponse.SOFTWARE_DEV,
                workExperience = emptyList(),
            ),
            status = ApplicationStatusResponse.APPLIED,
            coverLetter = "Cover letter",
            createdAt = instant,
            updatedAt = instant,
        )
    }
}
