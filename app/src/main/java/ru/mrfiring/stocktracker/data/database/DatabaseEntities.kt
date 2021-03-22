package ru.mrfiring.stocktracker.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DatabaseStockSymbol(
    @PrimaryKey(autoGenerate = false)
    val displaySymbol: String,
    val description: String,
    val currency: String,
    val figi: String,
    val mic: String,
    val type: String,
    val isFavorite: Boolean
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

@Entity
data class DatabaseSearchHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val query: String
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
data class DatabaseCompanyNews(
    val symbol: String,
    val category: String,
    val datetime: Long,
    val headline: String,
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val imgUrl: String,
    val sourceName: String,
    val summary: String,
    val articleUrl: String
)