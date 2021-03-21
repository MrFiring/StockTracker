package ru.mrfiring.stocktracker.data

import ru.mrfiring.stocktracker.data.database.DatabaseStockQuote
import ru.mrfiring.stocktracker.data.database.DatabaseStockSymbol
import ru.mrfiring.stocktracker.data.database.relations.StockSymbolAndQuote
import ru.mrfiring.stocktracker.data.network.StockQuote
import ru.mrfiring.stocktracker.data.network.StockSearchItem
import ru.mrfiring.stocktracker.data.network.StockSymbol
import ru.mrfiring.stocktracker.domain.DomainQuote
import ru.mrfiring.stocktracker.domain.DomainStockSearchItem
import ru.mrfiring.stocktracker.domain.DomainStockSymbol

fun StockSymbol.asDatabaseObject(): DatabaseStockSymbol {
    return DatabaseStockSymbol(displaySymbol, description, currency, figi, mic, type, false)
}

fun StockSymbolAndQuote.asDomainObject(
    logoUrl: String
): DomainStockSymbol {
    return DomainStockSymbol(
        stockSymbol.displaySymbol,
        stockSymbol.description,
        logoUrl,
        stockQuote?.asDomainModel() ?: DomainQuote(0.0, 0.0, 0.0, 0.0, 0.0),
        stockSymbol.isFavorite
    )
}

fun StockQuote.asDatabaseObject(symbol: String): DatabaseStockQuote {
    return DatabaseStockQuote(symbol, current, dayHigh, dayLow, dayOpen, previousDayOpen, time)
}

fun DatabaseStockQuote.asDomainModel(): DomainQuote =
    DomainQuote(current, dayHigh, dayLow, dayOpen, previousDayOpen)

fun StockSearchItem.asDomainObject(): DomainStockSearchItem =
    DomainStockSearchItem(description, displaySymbol, type)