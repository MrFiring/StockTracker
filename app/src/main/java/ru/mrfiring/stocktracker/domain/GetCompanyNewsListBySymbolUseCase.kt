package ru.mrfiring.stocktracker.domain

import javax.inject.Inject

class GetCompanyNewsListBySymbolUseCase @Inject constructor(
    private val companyRepository: CompanyRepository
) {
    suspend operator fun invoke(symbol: String): List<DomainCompanyNews> {
        return companyRepository.getCompanyNewsListBySymbol(symbol)
    }
}