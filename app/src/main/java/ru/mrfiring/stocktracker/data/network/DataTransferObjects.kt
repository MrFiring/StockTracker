package ru.mrfiring.stocktracker.data.network

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import ru.mrfiring.stocktracker.data.database.DatabaseCompany
import ru.mrfiring.stocktracker.data.database.DatabaseStockQuote
import ru.mrfiring.stocktracker.data.database.DatabaseStockSymbol
import java.util.function.LongToDoubleFunction

/*[
   {
    "currency": "USD",
    "description": "UAN POWER CORP",
    "displaySymbol": "UPOW",
    "figi": "BBG000BGHYF2",
    "mic": "OTCM",
    "symbol": "UPOW",
    "type": "Common Stock"
    }
]
*/

@JsonClass(generateAdapter = true)
data class StockSymbol(
    val currency : String,
    val description : String,
    val displaySymbol : String,
    val figi: String,
    val mic: String,
    val symbol: String,
    val type: String
){
    fun asDatabaseObject(): DatabaseStockSymbol {
        return DatabaseStockSymbol(displaySymbol, description, currency, figi, mic, type)
    }
}

/*
  {
  "country": "US",
  "currency": "USD",
  "exchange": "NASDAQ/NMS (GLOBAL MARKET)",
  "ipo": "1980-12-12",
  "marketCapitalization": 1415993,
  "name": "Apple Inc",
  "phone": "14089961010",
  "shareOutstanding": 4375.47998046875,
  "ticker": "AAPL",
  "weburl": "https://www.apple.com/",
  "logo": "https://static.finnhub.io/logo/87cb30d8-80df-11ea-8951-00000000092a.png",
  "finnhubIndustry":"Technology"
}
 */

@JsonClass(generateAdapter = true)
data class CompanyProfile(
    val country: String,
    val currency: String,
    val exchange: String,
    val ipo: String,
    val marketCapitalization: Double,
    val name: String,
    val phone: String,
    val shareOutStanding: Double,
    val ticker: String,
    @Json(name = "weburl") val webUrl: String,
    @Json(name = "logo") val logoUrl: String,
    val finhubIndustry: String
){
    fun asDatabaseObject(symbol: String): DatabaseCompany {
        return DatabaseCompany(symbol, exchange, ipo, marketCapitalization, name, phone, shareOutStanding, webUrl, logoUrl, finhubIndustry)
    }
}

/*
{
    "c": 261.74,
    "h": 263.31,
    "l": 260.68,
    "o": 261.07,
    "pc": 259.45,
    "t": 1582641000
}*/

@JsonClass(generateAdapter = true)
data class StockQuote(
        @Json(name = "c") val current: Double,
        @Json(name = "h") val dayHigh: Double,
        @Json(name = "l") val dayLow: Double,
        @Json(name = "o") val dayOpen: Double,
        @Json(name = "pc") val previousDayOpen: Double,
        @Json(name = "t") val time: Double
){
    fun asDatabaseObject(symbol: String): DatabaseStockQuote {
        return DatabaseStockQuote(symbol, current, dayHigh, dayLow, dayOpen, previousDayOpen, time.toDouble())
    }
}





