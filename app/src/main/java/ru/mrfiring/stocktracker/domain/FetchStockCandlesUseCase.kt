package ru.mrfiring.stocktracker.domain

import javax.inject.Inject

class FetchStockCandlesUseCase @Inject constructor(
    private val stockCandlesRepository: StockCandlesRepository
) {

    suspend operator fun invoke(
        symbol: String,
        resolution: String,
        fromTime: Long,
        toTime: Long
    ) {
        stockCandlesRepository.fetchStockCandles(
            symbol,
            resolution,
            fromTime,
            toTime
        )
    }
}