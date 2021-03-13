package ru.mrfiring.stocktracker.domain

import javax.inject.Inject

class RefreshStocksUseCase @Inject constructor(
    private val stockRepository: StockRepository
) {
    suspend operator fun invoke() = stockRepository.refreshStocks()
}