package ru.mrfiring.stocktracker.repository.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import ru.mrfiring.stocktracker.repository.database.DatabaseStockQuote
import ru.mrfiring.stocktracker.repository.database.DatabaseStockSymbol

data class StockSymbolAndQuote(
    @Embedded val stockSymbol: DatabaseStockSymbol,
    @Relation(
        parentColumn = "displaySymbol",
        entityColumn = "displaySymbol"
    )
    val stockQuote: DatabaseStockQuote?
)