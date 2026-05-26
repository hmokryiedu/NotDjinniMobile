package not.djinni.data.repository

import kotlinx.coroutines.runBlocking
import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.employer.EmployerDataSource
import not.djinni.network.employer.request.CreateEmployerProfileRequest
import not.djinni.network.employer.request.UpdateEmployerProfileRequest
import not.djinni.network.employer.response.EmployerProfileResponse
import not.djinni.network.vacancy.response.VacancyListResponse
import org.junit.Assert.assertEquals
import org.junit.Test

class DefaultEmployerRepositoryTest {

    @Test
    fun getEmployerVacancies_forwards_search_to_data_source() = runBlocking {
        val recorder = RecordingEmployerDataSource()
        val repository = DefaultEmployerRepository(recorder)

        repository.getEmployerVacancies("android")

        assertEquals("android", recorder.lastSearch)
    }

    @Test
    fun getEmployerVacancies_with_null_search_works() = runBlocking {
        val recorder = RecordingEmployerDataSource()
        val repository = DefaultEmployerRepository(recorder)

        val result = repository.getEmployerVacancies(null)

        assertEquals(null, recorder.lastSearch)
        assertEquals(true, result.isSuccess)
    }
}

private class RecordingEmployerDataSource : EmployerDataSource {
    var lastSearch: String? = null

    override suspend fun getProfile(): NetworkResponse<EmployerProfileResponse> = error("Not used")

    override suspend fun createProfile(request: CreateEmployerProfileRequest): NetworkResponse<EmployerProfileResponse> =
        error("Not used")

    override suspend fun updateProfileRole(request: UpdateEmployerProfileRequest): NetworkResponse<EmployerProfileResponse> =
        error("Not used")

    override suspend fun getVacancies(
        limit: Int,
        offset: Int,
        search: String?,
    ): NetworkResponse<VacancyListResponse> {
        lastSearch = search
        return NetworkResponse.Success(VacancyListResponse(vacancies = emptyList()))
    }
}
