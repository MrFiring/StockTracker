package ru.mrfiring.stocktracker.repository.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val TOKEN = "c0lvlln48v6p8fvj0ceg"
private const val BASE_URL = "https://finnhub.io/api/v1/"

interface StockService{
    @GET("stock/symbol")
    fun getStockSymbols(
        @Query("exchange") exchange: String,
        @Query("token") token: String = TOKEN
    ) : Call<List<StockSymbol>>

    @GET("stock/profile2")
    fun getCompanyProfile(
        @Query("symbol") symbol: String,
        @Query("token") token: String = TOKEN
    ): Call<CompanyProfile>

    @GET("quote")
    fun getStockQuote(
            @Query("symbol") symbol: String,
            @Query("token") token: String = TOKEN
    ): Call<StockQuote>
}

object FinhubNetwork{

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val finhub = retrofit.create(StockService::class.java)
}