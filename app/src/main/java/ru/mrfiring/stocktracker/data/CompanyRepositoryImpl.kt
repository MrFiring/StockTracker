package ru.mrfiring.stocktracker.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.mrfiring.stocktracker.data.database.CompanyDao
import ru.mrfiring.stocktracker.data.network.CompanyService
import ru.mrfiring.stocktracker.domain.CompanyRepository
import ru.mrfiring.stocktracker.domain.DomainCompany
import ru.mrfiring.stocktracker.domain.DomainCompanyNews
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

    override suspend fun fetchCompanyNewsList(symbol: String, fromDate: String, toDate: String) {
        withContext(Dispatchers.IO) {
            val newsList = service.getCompanyNewsList(symbol, fromDate, toDate)
            companyDao.insertCompanyNewsList(newsList.map {
                it.asDatabaseObject()
            })
        }
    }

    override suspend fun getCompanyNewsListBySymbol(symbol: String): List<DomainCompanyNews> {
        return companyDao.getCompanyNewsList(symbol).map {
            it.asDomainObject()
        }
    }
}