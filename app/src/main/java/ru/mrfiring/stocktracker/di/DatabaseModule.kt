package ru.mrfiring.stocktracker.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.mrfiring.stocktracker.data.database.CompanyDao
import ru.mrfiring.stocktracker.data.database.StockDao
import ru.mrfiring.stocktracker.data.database.StockDatabase
import javax.inject.Singleton

@Module
@InstallIn(ActivityComponent::class)
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
    fun provideCompanyDao(stockDatabase: StockDatabase): CompanyDao {
        return stockDatabase.companyDao
    }

}