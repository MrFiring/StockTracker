package ru.mrfiring.stocktracker.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.paging.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.mrfiring.stocktracker.data.database.DatabaseSearchHistory
import ru.mrfiring.stocktracker.data.database.DatabaseStockQuote
import ru.mrfiring.stocktracker.data.database.StockDao
import ru.mrfiring.stocktracker.data.network.BASE_LOGO_URL
import ru.mrfiring.stocktracker.data.network.StockSearchResult
import ru.mrfiring.stocktracker.data.network.StockService
import ru.mrfiring.stocktracker.data.paging.StockMediator
import ru.mrfiring.stocktracker.domain.DomainStockSearchItem
import ru.mrfiring.stocktracker.domain.DomainStockSymbol
import ru.mrfiring.stocktracker.domain.StockRepository
import javax.inject.Inject

class StockRepositoryImpl @ExperimentalPagingApi
@Inject constructor(
    private val stockDao: StockDao,
    private val stockService: StockService,
    private val stockMediator: StockMediator
) : StockRepository {
    @ExperimentalPagingApi
    override fun getStocksAndQuotesLiveData(): LiveData<PagingData<DomainStockSymbol>> {
        val pagingSourceFactory = {
            stockDao.getStocksAndQuotes()
        }
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 130
            ),
            pagingSourceFactory = pagingSourceFactory,
            remoteMediator = stockMediator,
        ).liveData.map {
            it.map { symbolAndQuote ->
                symbolAndQuote.asDomainObject(
                    logoUrl = "$BASE_LOGO_URL?symbol=${symbolAndQuote.stockSymbol.displaySymbol}"
                )
            }
        }

    }

    override suspend fun getFavoriteList(): List<DomainStockSymbol> {
        return stockDao.getFavoriteList().map {
            it.asDomainObject(
                logoUrl = "$BASE_LOGO_URL?symbol=${it.stockSymbol.displaySymbol}"
            )
        }
    }

    override suspend fun updateStockSymbol(item: DomainStockSymbol) {
        withContext(Dispatchers.IO) {
            stockDao.updateStockSymbol(item.symbol, item.isFavorite)
        }
    }

    override suspend fun refreshQuotes() {
        withContext(Dispatchers.IO) {
            val symbols = stockDao.getTickerList()
            val quotesTemp: MutableList<DatabaseStockQuote> = mutableListOf()
            symbols.forEachIndexed { index, symbol ->
                quotesTemp.add(
                    stockService.getStockQuote(symbol)
                        .asDatabaseObject(symbol)
                )

                //Insert every 20 items into db
                if (index > 0 && index % 20 == 0) {
                    Log.e(
                        "RepositoryStock",
                        "INSERTED ${quotesTemp.first().displaySymbol} ${quotesTemp.last().displaySymbol}"
                    )
                    stockDao.insertAllQuotes(quotesTemp)

                    quotesTemp.clear()
                }
            }
        }
    }

    override suspend fun getStockSearchHistory(): List<String> = stockDao.getStockSearchHistory()


    override suspend fun searchStockSymbol(query: String): List<DomainStockSearchItem> {
        var response = emptyList<DomainStockSearchItem>()
        withContext(Dispatchers.IO) {
            val searchResult: StockSearchResult = stockService.searchStockSymbol(query)
            stockDao.insertStockSearchHistory(
                DatabaseSearchHistory(query = query)
            )

            if (searchResult.count > 0) {
                response = searchResult.result.map {
                    it.asDomainObject()
                }
            }
        }

        return response
    }

    override suspend fun deleteAllSearchHistory() = stockDao.deleteAllStockSearchHistory()
}