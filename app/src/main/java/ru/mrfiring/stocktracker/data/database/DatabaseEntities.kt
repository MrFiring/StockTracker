package ru.mrfiring.stocktracker.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.mrfiring.stocktracker.data.database.relations.StockSymbolAndQuote
import ru.mrfiring.stocktracker.domain.DomainCompany
import ru.mrfiring.stocktracker.domain.DomainQuote
import ru.mrfiring.stocktracker.domain.DomainStockSymbol

@Entity
data class DatabaseStockSymbol(
    @PrimaryKey(autoGenerate = false)
    val displaySymbol: String,
    val description: String,
    val currency: String,
    val figi: String,
    val mic: String,
    val type: String
)

@Entity
data class DatabaseCompany(
    @PrimaryKey(autoGenerate = false)
    val displaySymbol: String,

    val exchange: String,
    val ipo: String,
    val marketCapitalization: Double,
    val name: String,
    val phone: String,
    val shareOutStanding: Double,
    val webUrl: String,
    val logoUrl: String,
    val finhubIndustry: String
)

@Entity
data class DatabaseStockQuote(
    @PrimaryKey(autoGenerate = false)
    val displaySymbol: String,

    val current: Double,
    val dayHigh: Double,
    val dayLow: Double,
    val dayOpen: Double,
    val previousDayOpen: Double,
    val time: Double
)

fun StockSymbolAndQuote.asDomainObject(
    logoUrl: String
): DomainStockSymbol {
    return DomainStockSymbol(
        stockSymbol.displaySymbol,
        stockSymbol.description,
        logoUrl,
        stockQuote?.asDomainModel() ?: DomainQuote(0.0, 0.0, 0.0, 0.0, 0.0)
    )
}

fun DatabaseCompany.asDomainObject(
): DomainCompany = DomainCompany(
    displaySymbol, exchange, marketCapitalization,
    name, phone, shareOutStanding, finhubIndustry
)

fun DatabaseStockQuote.asDomainModel(): DomainQuote =
    DomainQuote(current, dayHigh, dayLow, dayOpen, previousDayOpen)