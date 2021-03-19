package ru.mrfiring.stocktracker.data.network

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
    ): List<StockSymbol>

    @GET("quote")
    suspend fun getStockQuote(
        @Query("symbol") symbol: String,
        @Query("token") token: String = TOKEN
    ): StockQuote

    @GET("search")
    suspend fun searchStockSymbol(
        @Query("q") query: String,
        @Query("token") token: String = TOKEN
    ): StockSearchResult
}

interface CompanyService {
    @GET("stock/profile2")
    suspend fun getCompanyProfile(
        @Query("symbol") symbol: String,
        @Query("token") token: String = TOKEN
    ): CompanyProfile

    @GET("company-news")
    suspend fun getCompanyNewsList(
        @Query("symbol") symbol: String,
        @Query("from") fromDate: String,
        @Query("to") toDate: String,
        @Query("token") token: String = TOKEN
    ): List<CompanyNews>
}