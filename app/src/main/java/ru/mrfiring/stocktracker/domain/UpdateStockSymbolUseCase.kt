package ru.mrfiring.stocktracker.domain

import javax.inject.Inject

class UpdateStockSymbolUseCase @Inject constructor(
    private val stockRepository: StockRepository
) {
    suspend operator fun invoke(item: DomainStockSymbol) =
        stockRepository.updateStockSymbol(item)
}