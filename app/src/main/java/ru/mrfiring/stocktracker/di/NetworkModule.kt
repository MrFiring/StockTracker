package ru.mrfiring.stocktracker.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import org.joda.time.DateTime
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.mrfiring.stocktracker.data.network.BASE_URL
import ru.mrfiring.stocktracker.data.network.CompanyService
import ru.mrfiring.stocktracker.data.network.StockService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    //Created an interceptor to throttle requests and prevent 429 error.
    //API returns a period of time to wait before next request in header
    //Interceptor blocks the thread up to this time and send new request.
    //One second was added to result millis to avoid mistake with calculations.

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor {
                val response = it.proceed(it.request())

                if (!response.isSuccessful && response.code() == 429) {
                    val rateLimitReset = response.header("x-ratelimit-reset")
                    response.close()
                    rateLimitReset?.let { rateLim ->
                        val curTime = DateTime.now().millis / 1000L
                        val resetTime = rateLim.toLong() //it's in seconds UTC
                        val dt = resetTime - curTime
                        Thread.sleep((dt + 1) * 1000L)
                    }
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