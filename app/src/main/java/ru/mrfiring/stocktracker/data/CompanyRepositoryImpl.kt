package ru.mrfiring.stocktracker.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.mrfiring.stocktracker.data.database.CompanyDao
import ru.mrfiring.stocktracker.data.network.CompanyService
import ru.mrfiring.stocktracker.domain.CompanyRepository
import ru.mrfiring.stocktracker.domain.DomainCompany
import javax.inject.Inject

class CompanyRepositoryImpl @Inject constructor(
    private val companyDao: CompanyDao,
    private val service: CompanyService
) : CompanyRepository {
    override suspend fun getCompanyBySymbol(symbol: String): DomainCompany? {
        return companyDao.getCompanyBySymbol(symbol)?.asDomainObject()
    }

    override suspend fun fetchCompany(symbol: String) {
        withContext(Dispatchers.IO) {
            val company = service.getCompanyProfile(symbol)
            companyDao.insertCompany(
                company.asDatabaseObject(symbol)
            )
        }

    }
}