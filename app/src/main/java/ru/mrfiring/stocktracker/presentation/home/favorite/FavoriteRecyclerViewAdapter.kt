package ru.mrfiring.stocktracker.presentation.home.favorite

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.mrfiring.stocktracker.domain.DomainStockSymbol
import ru.mrfiring.stocktracker.presentation.reusable.diffutil.StockSymbolDiffUtilCallback
import ru.mrfiring.stocktracker.presentation.reusable.viewholder.StockViewHolder

class FavoriteRecyclerViewAdapter(
    private val onLongClick: (DomainStockSymbol) -> Unit,
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
            onLongClick = { onLongClick(item) },
            onClick = { onClick(item) }
        )
    }
}