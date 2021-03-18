package ru.mrfiring.stocktracker.domain

import javax.inject.Inject

class FetchCompanyUseCase @Inject constructor(
    private val companyRepository: CompanyRepository
) {
    suspend operator fun invoke(symbol: String) {
        companyRepository.fetchCompany(symbol)
    }
}