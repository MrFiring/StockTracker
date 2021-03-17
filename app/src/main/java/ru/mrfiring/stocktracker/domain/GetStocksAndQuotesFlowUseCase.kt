package ru.mrfiring.stocktracker.domain

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import javax.inject.Inject

class GetStocksAndQuotesLiveDataCase @Inject constructor(
    private val stockRepository: StockRepository
) {
    operator fun invoke(): LiveData<PagingData<DomainStockSymbol>> {
        return stockRepository.getStocksAndQuotesLiveData()
    }
}