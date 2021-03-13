package ru.mrfiring.stocktracker.data.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val TOKEN = "c0lvlln48v6p8fvj0ceg"
const val BASE_URL = "https://finnhub.io/api/v1/"
const val BASE_LOGO_URL = "https://finnhub.io/api/logo"

interface StockService{
    @GET("stock/symbol")
    suspend fun getStockSymbols(
        @Query("exchange") exchange: String = "US",
        @Query("token") token: String = TOKEN
    ) : List<StockSymbol>

    @GET("stock/profile2")
    suspend fun getCompanyProfile(
        @Query("symbol") symbol: String,
        @Query("token") token: String = TOKEN
    ): CompanyProfile

    @GET("quote")
    suspend fun getStockQuote(
            @Query("symbol") symbol: String,
            @Query("token") token: String = TOKEN
    ): StockQuote
}

object FinhubNetwork{

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val finhub = retrofit.create(StockService::class.java)
}