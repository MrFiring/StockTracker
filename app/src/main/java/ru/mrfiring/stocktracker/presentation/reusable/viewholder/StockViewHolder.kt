package ru.mrfiring.stocktracker.presentation.reusable.viewholder

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.avatarfirst.avatargenlib.AvatarConstants
import com.avatarfirst.avatargenlib.AvatarGenerator
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ru.mrfiring.stocktracker.R
import ru.mrfiring.stocktracker.databinding.StockListItemBinding
import ru.mrfiring.stocktracker.domain.DomainStockSymbol


class StockViewHolder private constructor(
    private val binding: StockListItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("StringFormatInvalid")
    fun bind(
        item: DomainStockSymbol,
        onLongClick: (DomainStockSymbol) -> Unit = {},
        onClick: (DomainStockSymbol) -> Unit
    ) {
        binding.apply {
            val context = root.context

            listItemCard.setOnClickListener {
                onClick(item)
            }

            listItemCard.setOnLongClickListener {
                onLongClick(item)
                true
            }

            companyName.text = item.companyName
            symbol.text = item.symbol

            val curPrice = item.quote.current
            currentPrice.text = context.getString(
                R.string.format_current_price,
                curPrice
            )

            if (curPrice > 0) {
                val dPrice = item.quote.getDeltaPrice()

                if (dPrice > 0) {
                    deltaPrice.setTextColor(Color.GREEN)
                } else if (dPrice < 0) {
                    deltaPrice.setTextColor(Color.RED)
                }

                deltaPrice.text = context.getString(
                    R.string.format_delta_price,
                    dPrice,
                    item.quote.getDeltaPricePercent()
                )
            }

            if (item.isFavorite) {
                favoriteMark.visibility = View.VISIBLE
            } else {
                favoriteMark.visibility = View.GONE
            }

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
