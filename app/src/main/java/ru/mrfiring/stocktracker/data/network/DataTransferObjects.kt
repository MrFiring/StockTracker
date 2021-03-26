package ru.mrfiring.stocktracker.data.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

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
    val description: String,
    val displaySymbol: String,
    val figi: String,
    val mic: String,
    val symbol: String,
    val type: String
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

/*
{
  "c": [
    133.07, ...
  ],
  "h": [
    133.24, ...
  ],
  "l": [
    129.77, ...
  ],
  "o": [
    130.33, ...
  ],
  "s": "ok", /// or no_data
  "t": [
    1616702402, ...
  ],
  "v": [
    5553966, ...
  ]
}

 */

@JsonClass(generateAdapter = true)
data class StockCandles(
    @Json(name = "o") val open: List<Double>?,
    @Json(name = "h") val high: List<Double>?,
    @Json(name = "l") val low: List<Double>?,
    @Json(name = "c") val close: List<Double>?,
    @Json(name = "v") val volume: List<Long>?,
    @Json(name = "t") val timestamp: List<Long>?,
    @Json(name = "s") val responseStatus: String
)


/*
{
    "count": 27,
    "result": [...]
}
 */

@JsonClass(generateAdapter = true)
data class StockSearchResult(
    val count: Int,
    val result: List<StockSearchItem>
)

/*
    {
      "description": "FLC STONE MINING AND INVESTM",
      "displaySymbol": "AMD.VN",
      "symbol": "AMD.VN",
      "type": "Common Stock"
    },
 */

@JsonClass(generateAdapter = true)
data class StockSearchItem(
    val description: String,
    val displaySymbol: String,
    val type: String
)

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
    val country: String?,
    val currency: String?,
    val exchange: String?,
    val ipo: String?,
    val marketCapitalization: Double?,
    val name: String?,
    val phone: String?,
    @Json(name = "shareOutstanding") val shareOutStanding: Double?,
    val ticker: String?,
    @Json(name = "weburl") val webUrl: String?,
    @Json(name = "logo") val logoUrl: String?,
    val finnhubIndustry: String?
)

/*
//{
//    "category": "company news",
//    "datetime": 1569550360,
//    "headline": "More sops needed to boost electronic manufacturing: Top govt official More sops needed to boost electronic manufacturing: Top govt official.  More sops needed to boost electronic manufacturing: Top govt official More sops needed to boost electronic manufacturing: Top govt official",
//    "id": 25286,
//    "image": "https://img.etimg.com/thumb/msid-71321314,width-1070,height-580,imgsize-481831,overlay-economictimes/photo.jpg",
//    "related": "AAPL",
//    "source": "The Economic Times India",
//    "summary": "NEW DELHI | CHENNAI: India may have to offer electronic manufacturers additional sops such as cheap credit and incentives for export along with infrastructure support in order to boost production and help the sector compete with China, Vietnam and Thailand, according to a top government official.These incentives, over and above the proposed reduction of corporate tax to 15% for new manufacturing units, are vital for India to successfully attract companies looking to relocate manufacturing facilities.“While the tax announcements made last week send a very good signal, in order to help attract investments, we will need additional initiatives,” the official told ET, pointing out that Indian electronic manufacturers incur 8-10% higher costs compared with other Asian countries.Sops that are similar to the incentives for export under the existing Merchandise Exports from India Scheme (MEIS) are what the industry requires, the person said.MEIS gives tax credit in the range of 2-5%. An interest subvention scheme for cheaper loans and a credit guarantee scheme for plant and machinery are some other possible measures that will help the industry, the official added.“This should be 2.0 (second) version of the electronic manufacturing cluster (EMC) scheme, which is aimed at creating an ecosystem with an anchor company plus its suppliers to operate in the same area,” he said.Last week, finance minister Nirmala Sitharaman announced a series of measures to boost economic growth including a scheme allowing any new manufacturing company incorporated on or after October 1, to pay income tax at 15% provided the company does not avail of any other exemption or incentives.",
//    "url": "https://economictimes.indiatimes.com/industry/cons-products/electronics/more-sops-needed-to-boost-electronic-manufacturing-top-govt-official/articleshow/71321308.cms"
//}
 */

@JsonClass(generateAdapter = true)
data class CompanyNews(
    @Json(name = "related")
    val symbol: String,
    val category: String,
    val datetime: Long,
    val headline: String,
    val id: Long,
    @Json(name = "image")
    val imgUrl: String,
    @Json(name = "source")
    val sourceName: String,
    val summary: String,
    @Json(name = "url")
    val articleUrl: String
)



