package ru.mrfiring.stocktracker.presentation.search

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.mrfiring.stocktracker.domain.DomainStockSymbol
import ru.mrfiring.stocktracker.presentation.reusable.diffutil.StockSymbolDiffUtilCallback
import ru.mrfiring.stocktracker.presentation.reusable.viewholder.StockViewHolder

class SearchResultsRecyclerViewAdapter(
    private val onClick: (DomainStockSymbol) -> Unit
) : ListAdapter<DomainStockSymbol, StockViewHolder>(StockSymbolDiffUtilCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        return StockViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(
            item = item,
            position = position,
            onClick = onClick
        )
    }

}