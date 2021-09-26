package ru.mrfiring.stocktracker.domain

import javax.inject.Inject

class GetStockAndQuoteBySymbolUseCase @Inject constructor(
    private val stockRepository: StockRepository
) {
    suspend operator fun invoke(symbol: String): DomainStockSymbol? {
        return stockRepository.getStockAndQuoteBySymbol(symbol)
    }
}