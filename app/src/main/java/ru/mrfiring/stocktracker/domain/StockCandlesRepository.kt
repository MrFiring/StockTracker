package ru.mrfiring.stocktracker.domain

interface StockCandlesRepository {

    suspend fun fetchStockCandles(
        symbol: String,
        resolution: String,
        fromTime: Long,
        toTime: Long
    )

    suspend fun getStockCandles(symbol: String, resolution: String): DomainStockCandles
}