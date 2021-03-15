package ru.mrfiring.stocktracker.domain

import kotlinx.coroutines.flow.Flow

interface StockRepository {

    fun getStocksAndQuotesFlow(): Flow<List<DomainStockSymbol>>

    suspend fun refreshStocks()

    suspend fun refreshQuotes()

}