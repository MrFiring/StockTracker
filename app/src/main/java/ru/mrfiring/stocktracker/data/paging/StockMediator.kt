package ru.mrfiring.stocktracker.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import retrofit2.HttpException
import ru.mrfiring.stocktracker.data.database.DatabaseStockSymbol
import ru.mrfiring.stocktracker.data.database.StockDao
import ru.mrfiring.stocktracker.data.network.StockService
import java.io.IOException
import javax.inject.Inject

@ExperimentalPagingApi
class StockMediator @Inject constructor(
    private val stockDao: StockDao,
    private val stockService: StockService
) : RemoteMediator<Int, DatabaseStockSymbol>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, DatabaseStockSymbol>
    ): MediatorResult {
        when (loadType) {
            LoadType.REFRESH -> {
                val symbolsCount = stockDao.getStocksAndQuotesCount()
                if (symbolsCount > 0) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                return try {
                    val symbolsList = stockService.getStockSymbols()

                    stockDao.insertAll(symbolsList.map {
                        it.asDatabaseObject()
                    })

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