package not.djinni.network.company

import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.company.response.CompanyListResponse

interface CompanyDataSource {
    suspend fun searchCompanies(query: String): NetworkResponse<CompanyListResponse>
}
