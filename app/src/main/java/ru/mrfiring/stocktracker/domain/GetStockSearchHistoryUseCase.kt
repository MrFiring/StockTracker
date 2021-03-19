package ru.mrfiring.stocktracker.domain

import javax.inject.Inject

class GetStockSearchHistoryUseCase @Inject constructor(
    private val stockRepository: StockRepository
) {
    suspend operator fun invoke(): List<String> = stockRepository.getStockSearchHistory()
}