package ru.mrfiring.stocktracker.repository

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
    val symbols: LiveData<List<DomainStockSymbol>> = Transformations.map(database.stockDao.getStocks()){
        it.map {dbStockSymbol ->
            dbStockSymbol.asDomainObject(
                    dbStockSymbol.description,
                    "${BASE_LOGO_URL}?symbol=${dbStockSymbol.displaySymbol}",
                    0.0,
                    0.0
            )
        }
    }

    suspend fun refreshStocks(){
        withContext(Dispatchers.IO){
            val stockList = FinhubNetwork.finhub.getStockSymbols("US")
            database.stockDao.insertAll(stockList.map { it.asDatabaseObject() })
        }
    }
}