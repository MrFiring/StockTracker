package ru.mrfiring.stocktracker.domain

import javax.inject.Inject


class RefreshQuotesUseCase @Inject constructor(
    private val stockRepository: StockRepository
) {
    suspend operator fun invoke() = stockRepository.refreshQuotes()
}