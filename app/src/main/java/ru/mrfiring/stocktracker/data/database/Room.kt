package ru.mrfiring.stocktracker.data.database

import androidx.paging.PagingSource
import androidx.room.*
import ru.mrfiring.stocktracker.data.database.relations.StockSymbolAndQuote

@Dao
interface StockDao {
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
interface CompanyDao {
    @Query("select * from databasecompany where displaySymbol = :symbol")
    suspend fun getCompanyBySymbol(symbol: String): DatabaseCompany?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompany(company: DatabaseCompany)

    @Query("select * from databasecompanynews where symbol = :symbol order by datetime desc")
    suspend fun getCompanyNewsList(symbol: String): List<DatabaseCompanyNews>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompanyNewsList(items: List<DatabaseCompanyNews>)

    @Query("delete from databasecompanynews")
    suspend fun deleteAllCompanyNews()
}


@Database(
    entities = [
        DatabaseStockSymbol::class,
        DatabaseCompany::class,
        DatabaseStockQuote::class,
        DatabaseCompanyNews::class
    ],
    version = 3
)
abstract class StockDatabase : RoomDatabase() {
    abstract val stockDao: StockDao
    abstract val companyDao: CompanyDao
}

