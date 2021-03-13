package ru.mrfiring.stocktracker.domain

import kotlinx.coroutines.flow.Flow

interface StockRepository {

    suspend fun getStocksAndQuotesFlow(): Flow<List<DomainStockSymbol>>

    suspend fun refreshStocks()
}