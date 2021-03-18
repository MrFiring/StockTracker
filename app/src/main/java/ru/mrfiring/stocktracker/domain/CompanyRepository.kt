package ru.mrfiring.stocktracker.domain

interface CompanyRepository {
    suspend fun getCompanyBySymbol(symbol: String): DomainCompany?

    suspend fun fetchCompany(symbol: String)

    suspend fun fetchCompanyNewsList(symbol: String, fromDate: String, toDate: String)

    suspend fun getCompanyNewsListBySymbol(symbol: String): List<DomainCompanyNews>
}