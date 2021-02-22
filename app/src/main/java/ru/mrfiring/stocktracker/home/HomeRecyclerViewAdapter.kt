package ru.mrfiring.stocktracker.home

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.mrfiring.stocktracker.repository.network.StockSymbol

class HomeRecyclerViewAdapter : ListAdapter<StockSymbol, >() {

    class StockViewHolder private constructor(val binding: DataBinding) : RecyclerView.ViewHolder()

}