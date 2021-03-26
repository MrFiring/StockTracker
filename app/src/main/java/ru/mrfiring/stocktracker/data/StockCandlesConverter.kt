package ru.mrfiring.stocktracker.data

import ru.mrfiring.stocktracker.data.database.DatabaseStockCandle
import ru.mrfiring.stocktracker.data.network.StockCandles
import ru.mrfiring.stocktracker.domain.DomainStockCandles

fun StockCandles.asDatabaseObject(
    symbol: String,
    resolution: String
): List<DatabaseStockCandle> {
    val list = mutableListOf<DatabaseStockCandle>()

    //All of the lists have to be the same length
    if (!open.isNullOrEmpty() && !high.isNullOrEmpty() &&
        !close.isNullOrEmpty() && !volume.isNullOrEmpty() &&
        !low.isNullOrEmpty() && !timestamp.isNullOrEmpty()
    ) {
        timestamp.forEachIndexed { i, stamp ->
            list.add(
                DatabaseStockCandle(
                    id = i,
                    symbol = symbol,
                    resolution = resolution,
                    open = open[i],
                    high = high[i],
                    low = low[i],
                    close = close[i],
                    volume = volume[i],
                    timestamp = stamp
                )
            )
        }

    }

    return list
}

fun List<DatabaseStockCandle>.asDomainObject(): DomainStockCandles {
    return DomainStockCandles(
        symbol = get(0).symbol,
        resolution = get(0).resolution,
        open = map { it.open },
        high = map { it.high },
        low = map { it.low },
        close = map { it.close },
        volume = map { it.volume },
        timestamp = map { it.timestamp },
    )
}