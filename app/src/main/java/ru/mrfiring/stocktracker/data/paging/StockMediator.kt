package ru.mrfiring.stocktracker.data.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.mrfiring.stocktracker.data.asDatabaseObject
import ru.mrfiring.stocktracker.data.database.StockDao
import ru.mrfiring.stocktracker.data.database.relations.StockSymbolAndQuote
import ru.mrfiring.stocktracker.data.network.StockService
import java.io.IOException
import javax.inject.Inject

@ExperimentalPagingApi
class StockMediator @Inject constructor(
    private val stockDao: StockDao,
    private val stockService: StockService
) : RemoteMediator<Int, StockSymbolAndQuote>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StockSymbolAndQuote>
    ): MediatorResult {
        Log.e("Mediator", loadType.toString())
        when (loadType) {
            LoadType.REFRESH -> {
                val symbolsCount = stockDao.getStocksAndQuotesCount()
                if (symbolsCount > 0) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                return try {

                    withContext(Dispatchers.IO) {
                        val symbolsList = stockService.getStockSymbols()
                        stockDao.insertAll(symbolsList.map {
                            it.asDatabaseObject()
                        })
                    }

                    MediatorResult.Success(endOfPaginationReached = false)
                } catch (ex: IOException) {
                    return MediatorResult.Error(ex)
                } catch (ex: HttpException) {
                    return MediatorResult.Error(ex)
                }


            }
            else -> {
                return MediatorResult.Success(endOfPaginationReached = true)
            }
        }
    }
}