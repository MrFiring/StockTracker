package ru.mrfiring.stocktracker.presentation.home.stocks

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import ru.mrfiring.stocktracker.domain.DomainStockSymbol
import ru.mrfiring.stocktracker.presentation.reusable.diffutil.StockSymbolDiffUtilCallback
import ru.mrfiring.stocktracker.presentation.reusable.viewholder.StockViewHolder

class StocksRecyclerViewAdapter(
    private val onClick: (DomainStockSymbol) -> Unit,
    private val onLongClick: (DomainStockSymbol) -> Unit
) : PagingDataAdapter<DomainStockSymbol, StockViewHolder>(
    StockSymbolDiffUtilCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        return StockViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it, onClick = onClick, onLongClick = onLongClick)
        }
    }

}