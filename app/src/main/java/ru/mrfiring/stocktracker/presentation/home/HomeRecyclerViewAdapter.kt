package ru.mrfiring.stocktracker.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.avatarfirst.avatargenlib.AvatarConstants
import com.avatarfirst.avatargenlib.AvatarGenerator
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ru.mrfiring.stocktracker.databinding.StockListItemBinding
import ru.mrfiring.stocktracker.domain.DomainStockSymbol

class HomeRecyclerViewAdapter(val clickListener: ClickListener) :
    ListAdapter<DomainStockSymbol, HomeRecyclerViewAdapter.StockViewHolder>(
        StockSymbolDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        return StockViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    class StockViewHolder private constructor(
        private val binding: StockListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DomainStockSymbol, clickListener: ClickListener) {
            binding.apply {
                root.setOnClickListener {
                    clickListener.onClick(item)
                }
                companyName.text = item.companyName
                symbol.text = item.symbol
                currentPrice.text = item.currentPrice.toString()
                deltaPrice.text = item.deltaPrice.toString()

                val context = root.context
                val imgUri = item.logoUrl.toUri().buildUpon().scheme("https").build()
                Glide.with(context)
                    .load(imgUri)
                    .apply(
                        RequestOptions()
                            .placeholder(
                                AvatarGenerator.avatarImage(
                                    context,
                                    150,
                                    AvatarConstants.CIRCLE,
                                    item.symbol ?: ""
                                )
                            )
                    )
                    .into(symbolLogo)
            }
        }

        companion object {
            fun from(parent: ViewGroup): StockViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = StockListItemBinding.inflate(layoutInflater, parent, false)

                return StockViewHolder(binding)
            }
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