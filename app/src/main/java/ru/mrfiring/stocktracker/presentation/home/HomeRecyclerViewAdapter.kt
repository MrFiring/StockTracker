package ru.mrfiring.stocktracker.presentation.home

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import ru.mrfiring.stocktracker.domain.DomainStockSymbol
import ru.mrfiring.stocktracker.presentation.reusable.viewholder.StockViewHolder

class HomeRecyclerViewAdapter(private val clickListener: ClickListener) :
    PagingDataAdapter<DomainStockSymbol, StockViewHolder>(
        StockSymbolDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        return StockViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it, clickListener)
        }
    }

    class StockSymbolDiffCallback : DiffUtil.ItemCallback<DomainStockSymbol>() {
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

    class ClickListener(val block: (DomainStockSymbol) -> Unit) {
        fun onClick(stock: DomainStockSymbol) = block(stock)
    }
}