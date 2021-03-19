package ru.mrfiring.stocktracker.domain

import androidx.lifecycle.LiveData
import androidx.paging.PagingData

interface StockRepository {

    fun getStocksAndQuotesLiveData(): LiveData<PagingData<DomainStockSymbol>>

    suspend fun refreshQuotes()

    suspend fun getStockSearchHistory(): List<String>

    suspend fun searchStockSymbol(query: String): List<DomainStockSearchItem>

    suspend fun deleteAllSearchHistory()
}