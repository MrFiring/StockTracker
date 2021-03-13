package ru.mrfiring.stocktracker.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.mrfiring.stocktracker.data.network.BASE_URL
import ru.mrfiring.stocktracker.data.network.StockService
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
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