@file:OptIn(kotlin.time.ExperimentalTime::class)

package not.djinni.data.repository

import not.djinni.model.application.ApplicationStatus
import not.djinni.network.application.request.ApplicationStatusRequest
import not.djinni.network.common.response.MessageResponse
import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.employer.response.CompanyResponse
import not.djinni.network.vacancy.VacancyDataSource
import not.djinni.network.vacancy.request.CreateVacancyRequest
import not.djinni.network.vacancy.response.EmploymentTypeResponse
import not.djinni.network.vacancy.response.JobCategoryCodeResponse
import not.djinni.network.vacancy.response.VacancyDetailsResponse
import not.djinni.network.vacancy.response.VacancyListResponse
import not.djinni.network.vacancy.response.VacancyStatusCodeResponse
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.time.Instant

class DefaultVacancyRepositoryTest {

    @Test
    fun getAppliedVacancies_forwards_statuses_to_data_source() = runBlocking {
        val recorder = RecordingVacancyDataSource()
        val repository = DefaultVacancyRepository(recorder)

        repository.getAppliedVacancies(
            statuses = listOf(ApplicationStatus.APPLIED, ApplicationStatus.OFFER, ApplicationStatus.WITHDRAWN)
        )

        assertEquals(
            listOf(
                ApplicationStatusRequest.APPLIED,
                ApplicationStatusRequest.OFFER,
                ApplicationStatusRequest.WITHDRAWN
            ),
            recorder.lastStatuses
        )
    }
}

private class RecordingVacancyDataSource : VacancyDataSource {
    var lastStatuses: List<ApplicationStatusRequest> = emptyList()

    override suspend fun getAllVacancies(limit: Int, offset: Int, search: String?) = error("Not used")
    override suspend fun getVacancyById(id: Long) = error("Not used")
    override suspend fun getPublicVacancies(limit: Int, offset: Int, search: String?) = error("Not used")
    override suspend fun getPublicVacancyById(id: Long) = error("Not used")

    override suspend fun getAppliedVacancies(
        limit: Int,
        offset: Int,
        statuses: List<ApplicationStatusRequest>
    ): NetworkResponse<VacancyListResponse> {
        lastStatuses = statuses
        return NetworkResponse.Success(
            VacancyListResponse(
                vacancies = listOf(
                    VacancyDetailsResponse(
                        id = 1L,
                        applicationId = 11L,
                        company = CompanyResponse(
                            id = 2L,
                            name = "Test",
                            website = "https://example.com",
                            description = "Company"
                        ),
                        title = "Android Engineer",
                        description = "Build features",
                        viewsCount = 10,
                        applicationsCount = 1,
                        salaryMin = 1000,
                        salaryMax = 2000,
                        minExperienceYears = 1,
                        employmentType = EmploymentTypeResponse.FULL_TIME,
                        category = JobCategoryCodeResponse.SOFTWARE_DEV,
                        status = VacancyStatusCodeResponse.ACTIVE,
                        isFavorite = false,
                        createdAt = Instant.parse("2026-01-01T00:00:00Z"),
                        updatedAt = Instant.parse("2026-01-01T00:00:00Z"),
                    )
                )
            )
        )
    }

    override suspend fun getFavoriteVacancies(limit: Int, offset: Int) = error("Not used")
    override suspend fun addFavoriteVacancy(vacancyId: Long): NetworkResponse<MessageResponse> = error("Not used")
    override suspend fun removeFavoriteVacancy(vacancyId: Long): NetworkResponse<MessageResponse> = error("Not used")
    override suspend fun createVacancy(request: CreateVacancyRequest) = error("Not used")
}
