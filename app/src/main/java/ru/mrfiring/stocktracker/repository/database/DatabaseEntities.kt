package ru.mrfiring.stocktracker.repository.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.mrfiring.stocktracker.repository.domain.DomainQuote
import ru.mrfiring.stocktracker.repository.domain.DomainStockDetail
import ru.mrfiring.stocktracker.repository.domain.DomainStockSymbol
import ru.mrfiring.stocktracker.repository.network.StockQuote

@Entity
data class DatabaseStockSymbol(
        @PrimaryKey
        val displaySymbol: String,
        val description: String,
        val currency: String,
        val figi: String,
        val mic: String,
        val type: String
)

@Entity
data class DatabaseCompany(
        @PrimaryKey
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
        @PrimaryKey
        val displaySymbol: String,

        val current: Double,
        val dayHigh: Double,
        val dayLow: Double,
        val dayOpen: Double,
        val previousDayOpen: Double,
        val time: Double
)

fun DatabaseStockSymbol.asDomainObject(
        companyName: String,logoUrl: String, deltaPrice: Double, currentPrice: Double
): DomainStockSymbol {
        return DomainStockSymbol(displaySymbol, companyName, currentPrice, deltaPrice, logoUrl)
}

fun DatabaseCompany.asDomainObject(
        quote: DomainQuote): DomainStockDetail
= DomainStockDetail(displaySymbol, exchange, marketCapitalization,
name, phone, shareOutStanding, finhubIndustry, quote)

fun DatabaseStockQuote.asDomainModel(): DomainQuote = DomainQuote(dayHigh, dayLow, dayOpen, previousDayOpen)