package ru.mrfiring.stocktracker.domain

import javax.inject.Inject

class GetFavoriteListUseCase @Inject constructor(
    private val stockRepository: StockRepository
) {
    suspend operator fun invoke(): List<DomainStockSymbol> = stockRepository.getFavoriteList()
}