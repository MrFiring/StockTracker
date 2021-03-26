package ru.mrfiring.stocktracker.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.mrfiring.stocktracker.data.database.StockCandlesDao
import ru.mrfiring.stocktracker.data.network.StockService
import ru.mrfiring.stocktracker.domain.DomainStockCandles
import ru.mrfiring.stocktracker.domain.StockCandlesRepository
import javax.inject.Inject

class StockCandlesRepositoryImpl @Inject constructor(
    private val stockCandlesDao: StockCandlesDao,
    private val stockService: StockService
) : StockCandlesRepository {
    override suspend fun fetchStockCandles(
        symbol: String,
        resolution: String,
        fromTime: Long,
        toTime: Long
    ) {
        withContext(Dispatchers.IO) {
            val networkCandles = stockService.getStockCandles(symbol, resolution, fromTime, toTime)
            stockCandlesDao.deleteAllStockCandles(symbol, resolution)

            stockCandlesDao.insertAllStockCandles(
                networkCandles.asDatabaseObject(symbol, resolution)
            )
        }
    }

    override suspend fun getStockCandles(symbol: String, resolution: String): DomainStockCandles {
        return stockCandlesDao.getStockCandles(symbol, resolution).asDomainObject()
    }
}