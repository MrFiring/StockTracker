package ru.mrfiring.stocktracker.repository.domain

data class DomainStockSymbol(
        val symbol: String,
        val companyName: String,
        val currentPrice: Double,
        val deltaPrice: Double,
        val logoUrl: String,
        val quote: DomainQuote
)

data class DomainStockDetail(
    val symbol: String,
    val exchange: String,
    val marketCapitalization: Double,
    val name: String,
    val phone: String,
    val shareOutStanding: Double,
    val finhubIndustry: String,

    val quote: DomainQuote
)

data class DomainQuote(
        val dayHigh: Double,
        val dayLow: Double,
        val dayOpen: Double,
        val previousDayOpen: Double
)