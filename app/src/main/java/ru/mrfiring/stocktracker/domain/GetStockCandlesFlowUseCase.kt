package ru.mrfiring.stocktracker.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStockCandlesFlowUseCase @Inject constructor(
    private val stockCandlesRepository: StockCandlesRepository
) {
    operator fun invoke(symbol: String, resolution: String): Flow<DomainStockCandles> {
        return stockCandlesRepository.getStockCandlesFlow(symbol, resolution)
    }
}