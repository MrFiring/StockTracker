package ru.mrfiring.stocktracker.repository.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface StockDao{
    @Query("select * from databasestocksymbol")
    fun getStocks(): LiveData<List<DatabaseStockSymbol>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(stocks: List<DatabaseStockSymbol>)
}

@Dao
interface CompanyDao{
    @Query("select * from databasecompany where displaySymbol = :symbol")
    suspend fun getCompany(symbol: String): DatabaseCompany?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(company : DatabaseCompany)
}

@Dao
interface StockQuoteDao{
    @Query("select * from databasestockquote where displaySymbol = :symbol")
    suspend fun getQuote(symbol: String): DatabaseStockQuote?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(quote: DatabaseStockQuote)
}

@Database(entities = [DatabaseStockSymbol::class,
    DatabaseCompany::class, DatabaseStockQuote::class],
    version = 1)
abstract class StockDatabase: RoomDatabase(){
    abstract val stockDao: StockDao
    abstract val companyDao: CompanyDao
    abstract val stockQuoteDao: StockQuoteDao
}

private lateinit var INSTANCE: StockDatabase
fun getDatabase(context: Context): StockDatabase {
    synchronized(StockDatabase::class.java){
        if(!::INSTANCE.isInitialized){
            INSTANCE = Room.databaseBuilder(context.applicationContext,
            StockDatabase::class.java, "stock").build()
        }
    }
    return INSTANCE
}
