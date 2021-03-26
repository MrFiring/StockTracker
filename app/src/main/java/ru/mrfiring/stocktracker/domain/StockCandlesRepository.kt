package ru.mrfiring.stocktracker.domain

import kotlinx.coroutines.flow.Flow

interface StockCandlesRepository {

    suspend fun fetchStockCandles(
        symbol: String,
        resolution: String,
        fromTime: Long,
        toTime: Long
    )

    fun getStockCandlesFlow(symbol: String, resolution: String): Flow<DomainStockCandles>
}