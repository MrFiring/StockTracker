package ru.mrfiring.stocktracker.data.database

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import ru.mrfiring.stocktracker.data.database.relations.StockSymbolAndQuote

@Dao
interface StockDao{
    @Transaction
    @Query("select * from databasestocksymbol order by displaySymbol")
    fun getStocksAndQuotes(): Flow<List<StockSymbolAndQuote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(stocks: List<DatabaseStockSymbol>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllQuotes(quotes: List<DatabaseStockQuote>)
}

@Dao
interface CompanyDao{
    @Query("select * from databasecompany where displaySymbol = :symbol")
    suspend fun getCompany(symbol: String): DatabaseCompany?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompany(company: DatabaseCompany)
}


@Database(entities = [DatabaseStockSymbol::class,
    DatabaseCompany::class, DatabaseStockQuote::class],
    version = 2)
abstract class StockDatabase: RoomDatabase(){
    abstract val stockDao: StockDao
    abstract val companyDao: CompanyDao
}

private lateinit var INSTANCE: StockDatabase
fun getDatabase(context: Context): StockDatabase {
    synchronized(StockDatabase::class.java){
        if(!::INSTANCE.isInitialized){
            INSTANCE = Room.databaseBuilder(context.applicationContext,
            StockDatabase::class.java, "stock")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}
