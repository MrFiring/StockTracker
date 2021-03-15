package ru.mrfiring.stocktracker.domain

data class DomainStockSymbol(
    val symbol: String,
    val companyName: String,
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
    val current: Double,
    val dayHigh: Double,
    val dayLow: Double,
    val dayOpen: Double,
    val previousDayOpen: Double
) {
    fun getDeltaPrice(): Double = dayOpen - dayHigh
    fun getDeltaPricePercent(): Double = (dayOpen - dayHigh) / dayOpen
}