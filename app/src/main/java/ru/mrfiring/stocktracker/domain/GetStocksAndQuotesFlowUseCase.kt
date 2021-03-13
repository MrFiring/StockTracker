package ru.mrfiring.stocktracker.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStocksAndQuotesFlowUseCase @Inject constructor(
    private val stockRepository: StockRepository
) {
    suspend operator fun invoke(): Flow<List<DomainStockSymbol>> {
        return stockRepository.getStocksAndQuotesFlow()
    }
}