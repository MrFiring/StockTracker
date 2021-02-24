package ru.mrfiring.stocktracker.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.mrfiring.stocktracker.repository.database.StockDatabase
import ru.mrfiring.stocktracker.repository.database.asDomainObject
import ru.mrfiring.stocktracker.repository.domain.DomainStockSymbol
import ru.mrfiring.stocktracker.repository.network.BASE_LOGO_URL
import ru.mrfiring.stocktracker.repository.network.FinhubNetwork

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
            val stockList = FinhubNetwork.finhub.getStockSymbols("US")
            database.stockDao.insertAll(stockList.map { it.asDatabaseObject() })

            for(symbol in stockList){
               val quote = FinhubNetwork.finhub.getStockQuote(symbol.displaySymbol)
                Log.d("REFRESHSTOCKS", quote.toString())
                database.stockDao.insertQuote(quote.asDatabaseObject(symbol.displaySymbol))
            }

        }
    }
}