package not.djinni.domain.repository

import not.djinni.model.company.Company

interface CompanyRepository {
    suspend fun searchCompanies(query: String): List<Company>
}
