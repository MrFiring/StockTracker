package ru.mrfiring.stocktracker.domain

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.mrfiring.stocktracker.data.database.DatabaseStockQuote
import ru.mrfiring.stocktracker.data.database.StockDatabase
import ru.mrfiring.stocktracker.data.database.asDomainObject
import ru.mrfiring.stocktracker.data.network.BASE_LOGO_URL
import ru.mrfiring.stocktracker.data.network.FinhubNetwork

class StockRepository(private val database: StockDatabase) {
    val symbols: Flow<List<DomainStockSymbol>> = database.stockDao.getStocksAndQuotes().map {
        it.map { dbItem ->
            dbItem.asDomainObject("$BASE_LOGO_URL?symbol=${dbItem.stockSymbol.displaySymbol}")
        }
    }

    suspend fun refreshStocks(){
        withContext(Dispatchers.IO){
            var stockList = FinhubNetwork.finhub.getStockSymbols("US")

            database.stockDao.insertAll(stockList.map { it.asDatabaseObject() })

            stockList = stockList.sortedBy {
                it.displaySymbol
            }
            val quoteList = mutableListOf<DatabaseStockQuote>()

            //Todo do something with this counter variable.
            var counter = 0
            var callsCounter = 0
            var deltaTime:Long = System.currentTimeMillis()
            var absoluteTime: Long = 0
            for(symbol in stockList){
                try {
                    if(counter >= 10){
                        database.stockDao.insertAllQuotes(quoteList)
                        quoteList.clear()
                        Log.e("REFRESHSTOCKS", "||||WRITTEN||||")
                        counter = 0
                    }
                    deltaTime = System.currentTimeMillis()
                    val quote = FinhubNetwork.finhub.getStockQuote(symbol.displaySymbol)
                    quoteList.add(quote.asDatabaseObject(symbol.displaySymbol))
                    Log.d("REFRESHSTOCKS", "${symbol.symbol} |||| ${quote.toString()}")
                    counter++
                    callsCounter++
                    deltaTime = System.currentTimeMillis() - deltaTime
                    absoluteTime += deltaTime
                    if(callsCounter > 20 && absoluteTime >= 800){
                        Thread.sleep(300)
                        absoluteTime = 0
                    }

                }catch (httpException: HttpException){
                    Log.e("REFRESHSTOCKS", "error ${httpException.code()}")
                }
            }

        }
    }
}