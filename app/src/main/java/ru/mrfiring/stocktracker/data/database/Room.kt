package ru.mrfiring.stocktracker.data.database

import androidx.paging.PagingSource
import androidx.room.*
import ru.mrfiring.stocktracker.data.database.relations.StockSymbolAndQuote

@Dao
interface StockDao{
    @Transaction
    @Query("select * from databasestocksymbol order by displaySymbol")
    fun getStocksAndQuotes(): PagingSource<Int, StockSymbolAndQuote>

    @Query("select displaySymbol from databasestocksymbol order by displaySymbol")
    suspend fun getTickerList(): List<String>

    @Query("select count(*) from databasestocksymbol")
    suspend fun getStocksAndQuotesCount(): Int

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

