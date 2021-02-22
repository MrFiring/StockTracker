package ru.mrfiring.stocktracker

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.mrfiring.stocktracker.home.HomeRecyclerViewAdapter
import ru.mrfiring.stocktracker.repository.domain.DomainStockSymbol

@BindingAdapter("listData")
fun RecyclerView.setListData(list: List<DomainStockSymbol>?){
    val adpt = adapter as HomeRecyclerViewAdapter
    adpt.submitList(list)
}