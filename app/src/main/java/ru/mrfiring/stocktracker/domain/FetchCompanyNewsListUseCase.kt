package ru.mrfiring.stocktracker.domain

import javax.inject.Inject

class FetchCompanyNewsListUseCase @Inject constructor(
    private val companyRepository: CompanyRepository
) {
    suspend operator fun invoke(symbol: String, fromDate: String, toDate: String) {
        companyRepository.fetchCompanyNewsList(symbol, fromDate, toDate)
    }
}