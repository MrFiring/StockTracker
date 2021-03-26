package ru.mrfiring.stocktracker.domain

import javax.inject.Inject

class GetStockCandlesUseCase @Inject constructor(
    private val stockCandlesRepository: StockCandlesRepository
) {
    suspend operator fun invoke(symbol: String, resolution: String): DomainStockCandles {
        return stockCandlesRepository.getStockCandles(symbol, resolution)
    }
}