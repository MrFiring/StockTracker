package ru.mrfiring.stocktracker.domain

import javax.inject.Inject

class GetCompanyBySymbolUseCase @Inject constructor(
    private val companyRepository: CompanyRepository
) {
    suspend operator fun invoke(symbol: String): DomainCompany? {
        return companyRepository.getCompanyBySymbol(symbol)
    }
}