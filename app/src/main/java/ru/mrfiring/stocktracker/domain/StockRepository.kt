package ru.mrfiring.stocktracker.domain

import androidx.lifecycle.LiveData
import androidx.paging.PagingData

interface StockRepository {

    fun getStocksAndQuotesLiveData(): LiveData<PagingData<DomainStockSymbol>>

    suspend fun refreshQuotes()

}