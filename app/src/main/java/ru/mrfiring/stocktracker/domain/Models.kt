package ru.mrfiring.stocktracker.domain

data class DomainStockSymbol(
    val symbol: String,
    val companyName: String,
    val logoUrl: String,
    val quote: DomainQuote,
    val isFavorite: Boolean
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

data class DomainStockCandles(
    val symbol: String,
    val resolution: String,
    val open: List<Double>,
    val high: List<Double>,
    val low: List<Double>,
    val close: List<Double>,
    val volume: List<Long>,
    val timestamp: List<Long>,
)

data class DomainStockSearchItem(
    val description: String,
    val displaySymbol: String,
    val type: String
)

data class DomainCompany(
    val symbol: String,
    val exchange: String,
    val marketCapitalization: Double,
    val name: String,
    val phone: String,
    val shareOutStanding: Double,
    val finhubIndustry: String,
)

data class DomainCompanyNews(
    val symbol: String,
    val category: String,
    val datetime: Long,
    val headline: String,
    val id: Long,
    val imgUrl: String,
    val sourceName: String,
    val summary: String,
    val articleUrl: String
)
