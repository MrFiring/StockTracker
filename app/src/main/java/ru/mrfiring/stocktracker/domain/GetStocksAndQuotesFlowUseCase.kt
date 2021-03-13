package ru.mrfiring.stocktracker.domain

import kotlinx.coroutines.flow.Flow

class GetStocksAndQuotesFlowUseCase(private val stockRepository: StockRepository) {
    suspend operator fun invoke(): Flow<List<DomainStockSymbol>> {
        return stockRepository.getStocksAndQuotesFlow()
    }
}