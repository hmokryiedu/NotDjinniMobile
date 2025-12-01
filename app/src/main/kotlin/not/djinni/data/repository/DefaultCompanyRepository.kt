package not.djinni.data.repository

import not.djinni.data.mapper.toDomain
import not.djinni.domain.repository.CompanyRepository
import not.djinni.model.company.Company
import not.djinni.network.common.response.NetworkResponse
import not.djinni.network.company.CompanyDataSource
import org.koin.core.annotation.Single

@Single(binds = [CompanyRepository::class])
class DefaultCompanyRepository(
    private val remoteDataSource: CompanyDataSource,
) : CompanyRepository {

    override suspend fun searchCompanies(query: String): List<Company> {
        return when (val response = remoteDataSource.searchCompanies(query)) {
            is NetworkResponse.Success -> response.data.companies.map { it.toDomain() }
            is NetworkResponse.Error -> emptyList()
        }
    }
}
