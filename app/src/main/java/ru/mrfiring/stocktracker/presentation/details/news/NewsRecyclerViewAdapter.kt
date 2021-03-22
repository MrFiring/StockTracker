package ru.mrfiring.stocktracker.presentation.details.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ru.mrfiring.stocktracker.R
import ru.mrfiring.stocktracker.databinding.ItemNewsListBinding
import ru.mrfiring.stocktracker.domain.DomainCompanyNews

class NewsRecyclerViewAdapter(
    private val onClick: (DomainCompanyNews) -> Unit
) : ListAdapter<DomainCompanyNews, NewsRecyclerViewAdapter.NewsViewHolder>(
    NewsDiffUtilCallback()
) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onClick)
    }

    class NewsViewHolder(
        private val binding: ItemNewsListBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DomainCompanyNews, onClick: (DomainCompanyNews) -> Unit) {
            with(binding) {
                val context = root.context
                headLineText.text = item.headline
                sourceText.text = item.sourceName
                summaryText.text = item.summary

                val imgUri = item.imgUrl.toUri().buildUpon().scheme("https").build()
                Glide.with(context)
                    .load(imgUri)
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.ic_connection_error)
                    )
                    .into(newsImage)

            }
        }

        companion object {
            fun from(parent: ViewGroup): NewsViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ItemNewsListBinding.inflate(inflater, parent, false)
                return NewsViewHolder(binding)
            }
        }
    }

    class NewsDiffUtilCallback : DiffUtil.ItemCallback<DomainCompanyNews>() {
        override fun areItemsTheSame(
            oldItem: DomainCompanyNews,
            newItem: DomainCompanyNews
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: DomainCompanyNews,
            newItem: DomainCompanyNews
        ): Boolean {
            return oldItem == newItem
        }
    }
}