package ru.mrfiring.stocktracker.repository.network

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

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

@Parcelize
data class StockSymbol(
    val currency : String,
    val description : String,
    val displaySymbol : String,
    val figi: String,
    val mic: String,
    val symbol: String,
    val type: String
):Parcelable

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
)

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
)