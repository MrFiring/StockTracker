package ru.mrfiring.stocktracker.domain

import javax.inject.Inject

class SearchStockSymbolUseCase @Inject constructor(
    private val stockRepository: StockRepository
) {
    suspend operator fun invoke(query: String): List<DomainStockSearchItem> =
        stockRepository.searchStockSymbol(query)
}