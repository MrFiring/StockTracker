package ru.mrfiring.stocktracker.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.mrfiring.stocktracker.data.database.CompanyDao
import ru.mrfiring.stocktracker.data.database.StockCandlesDao
import ru.mrfiring.stocktracker.data.database.StockDao
import ru.mrfiring.stocktracker.data.database.StockDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): StockDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            StockDatabase::class.java, "stock"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideStockDao(stockDatabase: StockDatabase): StockDao {
        return stockDatabase.stockDao
    }

    @Singleton
    @Provides
    fun provideStockCandlesDao(stockDatabase: StockDatabase): StockCandlesDao {
        return stockDatabase.stockCandlesDao
    }

    @Singleton
    @Provides
    fun provideCompanyDao(stockDatabase: StockDatabase): CompanyDao {
        return stockDatabase.companyDao
    }

}