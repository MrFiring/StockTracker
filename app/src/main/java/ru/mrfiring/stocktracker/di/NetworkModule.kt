package ru.mrfiring.stocktracker.di

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.mrfiring.stocktracker.data.network.BASE_URL
import ru.mrfiring.stocktracker.data.network.CompanyService
import ru.mrfiring.stocktracker.data.network.StockService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor {
                val response = it.proceed(it.request())
                if (!response.isSuccessful && response.code() == 429) {
                    Thread.sleep(3000)
                    Log.e("HttpClient", "Error 429 caught")
                    response.close()
                    it.proceed(it.request())
                } else {
                    response
                }
            }
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(httpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .client(httpClient)
        .build()

    @Singleton
    @Provides
    fun provideStockService(retrofit: Retrofit): StockService {
        return retrofit.create(StockService::class.java)
    }

    @Singleton
    @Provides
    fun provideCompanyService(retrofit: Retrofit): CompanyService {
        return retrofit.create(CompanyService::class.java)
    }

}