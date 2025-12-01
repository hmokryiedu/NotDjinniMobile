package not.djinni.network.company

import io.ktor.client.HttpClient
import io.ktor.client.plugins.resources.get
import not.djinni.network.common.extension.networkResponse
import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.company.resource.Company
import not.djinni.network.company.response.CompanyListResponse
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Single(binds = [CompanyDataSource::class])
internal class DefaultCompanyDataSource(
    @Named("authenticated") private val httpClient: HttpClient
) : CompanyDataSource {

    override suspend fun searchCompanies(query: String): NetworkResponse<CompanyListResponse> {
        return httpClient
            .get(Company.Search(name = query))
            .networkResponse<CompanyListResponse>()
    }
}
