package ru.mrfiring.stocktracker.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.mrfiring.stocktracker.repository.database.DatabaseStockQuote
import ru.mrfiring.stocktracker.repository.database.StockDatabase
import ru.mrfiring.stocktracker.repository.database.asDomainObject
import ru.mrfiring.stocktracker.repository.domain.DomainStockSymbol
import ru.mrfiring.stocktracker.repository.network.BASE_LOGO_URL
import ru.mrfiring.stocktracker.repository.network.FinhubNetwork
import ru.mrfiring.stocktracker.repository.network.StockQuote

class StockRepository(private val database: StockDatabase) {
    val symbols: LiveData<List<DomainStockSymbol>> = Transformations.map(database.stockDao.getStocksAndQuotes()){
        it.map {dbStockSymbol ->

            dbStockSymbol.asDomainObject(
                    "${BASE_LOGO_URL}?symbol=${dbStockSymbol.stockSymbol.displaySymbol}"
            )
        }
    }

    suspend fun refreshStocks(){
        withContext(Dispatchers.IO){
            var stockList = FinhubNetwork.finhub.getStockSymbols("US")
            stockList = stockList.sortedBy {
                it.displaySymbol
            }
            database.stockDao.insertAll(stockList.map { it.asDatabaseObject() })

            val quoteList = mutableListOf<DatabaseStockQuote>()

            var counter = 0
            for(symbol in stockList){
                try {
                    if(counter >= 10){
                        database.stockDao.insertAllQuotes(quoteList)
                        quoteList.clear()
                        counter = 0
                    }

                    val quote = FinhubNetwork.finhub.getStockQuote(symbol.displaySymbol)
                    quoteList.add(quote.asDatabaseObject(symbol.displaySymbol))
                    Log.d("REFRESHSTOCKS", "${symbol.symbol} |||| ${quote.toString()}")
                    counter++
                }catch (httpException: HttpException){
                    Log.e("REFRESHSTOCKS", "error ${httpException.code()}")
                }
            }

        }
    }
}