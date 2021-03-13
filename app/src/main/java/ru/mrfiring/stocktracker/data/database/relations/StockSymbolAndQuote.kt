package ru.mrfiring.stocktracker.data.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import ru.mrfiring.stocktracker.data.database.DatabaseStockQuote
import ru.mrfiring.stocktracker.data.database.DatabaseStockSymbol

data class StockSymbolAndQuote(
    @Embedded val stockSymbol: DatabaseStockSymbol,
    @Relation(
        parentColumn = "displaySymbol",
        entityColumn = "displaySymbol"
    )
    val stockQuote: DatabaseStockQuote?
)