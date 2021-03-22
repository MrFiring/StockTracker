package ru.mrfiring.stocktracker.domain

import androidx.lifecycle.LiveData
import javax.inject.Inject

class GetFavoriteLiveDataUseCase @Inject constructor(
    private val stockRepository: StockRepository
) {
    operator fun invoke(): LiveData<List<DomainStockSymbol>> = stockRepository.getFavoriteLiveData()
}