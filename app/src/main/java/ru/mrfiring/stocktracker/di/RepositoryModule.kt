package ru.mrfiring.stocktracker.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.mrfiring.stocktracker.data.CompanyRepositoryImpl
import ru.mrfiring.stocktracker.data.StockRepositoryImpl
import ru.mrfiring.stocktracker.domain.CompanyRepository
import ru.mrfiring.stocktracker.domain.StockRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideStockRepository(stockRepositoryImpl: StockRepositoryImpl): StockRepository {
        return stockRepositoryImpl
    }

    @Singleton
    @Provides
    fun provideCompanyRepository(companyRepositoryImpl: CompanyRepositoryImpl): CompanyRepository {
        return companyRepositoryImpl
    }

}