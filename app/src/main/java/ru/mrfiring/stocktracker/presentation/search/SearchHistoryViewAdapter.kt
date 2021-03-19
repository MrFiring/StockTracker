package ru.mrfiring.stocktracker.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.mrfiring.stocktracker.databinding.SearchHistoryItemBinding

class SearchHistoryViewAdapter(
    private val onClick: (String) -> Unit
) : ListAdapter<String, SearchHistoryViewAdapter.SearchViewHolder>(SearchHistoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onClick)
    }

    class SearchViewHolder(
        private val binding: SearchHistoryItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String, onClick: (String) -> Unit) {
            binding.apply {
                chipSearchName.text = item
                chipSearchName.setOnClickListener { onClick(item) }
            }
        }

        companion object {
            fun from(parent: ViewGroup): SearchViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = SearchHistoryItemBinding.inflate(layoutInflater, parent, false)

                return SearchViewHolder(binding)
            }
        }
    }

    class SearchHistoryDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem.length == newItem.length
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}