package ru.mrfiring.stocktracker.domain

interface CompanyRepository {
    suspend fun getCompanyBySymbol(symbol: String): DomainCompany?

    suspend fun fetchCompany(symbol: String)
}