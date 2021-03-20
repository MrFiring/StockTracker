package ru.mrfiring.stocktracker.presentation.reusable.diffutil

import androidx.recyclerview.widget.DiffUtil
import ru.mrfiring.stocktracker.domain.DomainStockSymbol

class StockSymbolDiffUtilCallback : DiffUtil.ItemCallback<DomainStockSymbol>() {
    override fun areItemsTheSame(
        oldItem: DomainStockSymbol,
        newItem: DomainStockSymbol
    ): Boolean {
        return oldItem.symbol == newItem.symbol
    }

    override fun areContentsTheSame(
        oldItem: DomainStockSymbol,
        newItem: DomainStockSymbol
    ): Boolean {
        return oldItem == newItem
    }
}
