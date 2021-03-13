package ru.mrfiring.stocktracker.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.mrfiring.stocktracker.data.database.StockDao
import ru.mrfiring.stocktracker.data.database.asDomainObject
import ru.mrfiring.stocktracker.data.network.BASE_LOGO_URL
import ru.mrfiring.stocktracker.data.network.StockService
import ru.mrfiring.stocktracker.data.network.StockSymbol
import ru.mrfiring.stocktracker.domain.DomainStockSymbol
import ru.mrfiring.stocktracker.domain.StockRepository
import javax.inject.Inject

class StockRepositoryImpl @Inject constructor(
    private val stockDao: StockDao,
    private val stockService: StockService
) : StockRepository {
    override fun getStocksAndQuotesFlow(): Flow<List<DomainStockSymbol>> {
        return stockDao.getStocksAndQuotes().map {
            it.map { symbol ->
                symbol.asDomainObject(
                    logoUrl = "$BASE_LOGO_URL?symbol=${symbol.stockSymbol.displaySymbol}"
                )
            }
        }
    }

    override suspend fun refreshStocks() {
        withContext(Dispatchers.IO) {
            if (stockDao.getStocksAndQuotesCount() == 0) {
                val symbolsList: List<StockSymbol> = stockService.getStockSymbols()
                stockDao.insertAll(
                    symbolsList.map {
                        it.asDatabaseObject()
                    }
                )
            }
        }

    }
}