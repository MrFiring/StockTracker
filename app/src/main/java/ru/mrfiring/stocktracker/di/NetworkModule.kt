package ru.mrfiring.stocktracker.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.mrfiring.stocktracker.data.network.BASE_URL
import ru.mrfiring.stocktracker.data.network.StockService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    @Provides
    fun provideStockService(retrofit: Retrofit): StockService {
        return retrofit.create(StockService::class.java)
    }

}