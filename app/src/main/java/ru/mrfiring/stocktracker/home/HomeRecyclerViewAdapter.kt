package ru.mrfiring.stocktracker.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.mrfiring.stocktracker.databinding.StockListItemBinding
import ru.mrfiring.stocktracker.repository.domain.DomainStockSymbol

class HomeRecyclerViewAdapter(val clickListener: HomeRecyclerViewAdapter.ClickListener) : ListAdapter<DomainStockSymbol, HomeRecyclerViewAdapter.StockViewHolder>(StockSymbolDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        return StockViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    class StockViewHolder private constructor(val binding: StockListItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: DomainStockSymbol, clickListener: ClickListener){
            binding.stockSymbol = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): StockViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = StockListItemBinding.inflate(layoutInflater, parent, false)

                return StockViewHolder(binding)
            }
        }
    }

    class StockSymbolDiffCallback: DiffUtil.ItemCallback<DomainStockSymbol>(){
        override fun areItemsTheSame(oldItem: DomainStockSymbol, newItem: DomainStockSymbol): Boolean {
            return oldItem.symbol == newItem.symbol
        }

        override fun areContentsTheSame(oldItem: DomainStockSymbol, newItem: DomainStockSymbol): Boolean {
            return oldItem == newItem
        }
    }

    class ClickListener(val block: (DomainStockSymbol) -> Unit){
        fun onClick(stock: DomainStockSymbol) = block(stock)
    }
}