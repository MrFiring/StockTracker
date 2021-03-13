package ru.mrfiring.stocktracker.domain

class RefreshStocksUseCase(private val stockRepository: StockRepository) {
    suspend operator fun invoke() = stockRepository.refreshStocks()
}